package com.github.salvadormg15.rubber_duck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class RubberDuckBlock extends RedstoneDiodeBlock {
	public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

	public RubberDuckBlock() {
		super(Properties.of(Material.BAMBOO).strength(0.2f, 0.2f));
		this.registerDefaultState(
			this.stateDefinition.any()
				.setValue(POWERED, Boolean.valueOf(false))
				.setValue(TRIGGERED, Boolean.valueOf(false))
				.setValue(FACING, Direction.NORTH)
		);
		runCalculations(SHAPE);
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(POWERED, TRIGGERED, FACING);
	}

//  Redstone behavior
	@Override
	public ActionResultType use(BlockState p_225533_1_, World p_225533_2_, BlockPos p_225533_3_, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
		this.press(p_225533_1_, p_225533_2_, p_225533_3_);
		p_225533_2_.playSound(p_225533_4_, p_225533_3_, Registries.RUBBER_DUCK_USE.get(), SoundCategory.BLOCKS, 1.3f, 1f);
		return ActionResultType.sidedSuccess(p_225533_2_.isClientSide);
	}

	public void press(BlockState p_226910_1_, World p_226910_2_, BlockPos p_226910_3_) {
		p_226910_2_.setBlock(p_226910_3_, p_226910_1_.setValue(POWERED, Boolean.valueOf(true)).setValue(TRIGGERED, Boolean.valueOf(true)), 3);
		p_226910_2_.getBlockTicks().scheduleTick(p_226910_3_, this, this.getPressDuration());
	}

	@Override
	protected void updateNeighborsInFront(World p_176400_1_, BlockPos p_176400_2_, BlockState p_176400_3_) {
		for (Direction direction : Direction.values()) {
			if (net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(p_176400_1_, p_176400_2_, p_176400_1_.getBlockState(p_176400_2_), java.util.EnumSet.of(direction.getOpposite()), false).isCanceled())
				return;
			BlockPos blockpos = p_176400_2_.relative(direction.getOpposite());
			p_176400_1_.neighborChanged(blockpos, this, p_176400_2_);
			p_176400_1_.updateNeighborsAtExceptFromFacing(blockpos, this, direction);
		}
	}

	@Override
	public void tick(BlockState p_225534_1_, ServerWorld p_225534_2_, BlockPos p_225534_3_, Random p_225534_4_) {
		if (!this.isLocked(p_225534_2_, p_225534_3_, p_225534_1_)) {
			boolean flag = p_225534_1_.getValue(TRIGGERED);
			boolean flag1 = this.shouldTurnOn(p_225534_2_, p_225534_3_, p_225534_1_);
			if (flag && !flag1) {
				p_225534_2_.setBlock(p_225534_3_, p_225534_1_.setValue(TRIGGERED, Boolean.valueOf(false)), 2);
			} else if (!flag) {
				p_225534_2_.playSound(null, p_225534_3_, Registries.RUBBER_DUCK_USE.get(), SoundCategory.BLOCKS, 1.3f, 1f);
				p_225534_2_.setBlock(p_225534_3_, p_225534_1_.setValue(TRIGGERED, Boolean.valueOf(true)), 2);
				if (!flag1) {
					p_225534_2_.getBlockTicks().scheduleTick(p_225534_3_, this, this.getDelay(p_225534_1_), TickPriority.VERY_HIGH);
				}
			}

			boolean powered = p_225534_1_.getValue(POWERED);
			if (powered && !p_225534_2_.getBlockTicks().willTickThisTick(p_225534_3_, this)) {
				this.checkPressed(p_225534_1_, p_225534_2_, p_225534_3_);
			}
		}
	}

	@Override
	public void entityInside(BlockState p_196262_1_, World p_196262_2_, BlockPos p_196262_3_, Entity p_196262_4_) {
		if (!p_196262_2_.isClientSide && !p_196262_1_.getValue(POWERED)) {
			this.checkPressed(p_196262_1_, p_196262_2_, p_196262_3_);
		}
	}

	private void checkPressed(BlockState p_185616_1_, World p_185616_2_, BlockPos p_185616_3_) {
		List<? extends Entity> list = p_185616_2_.getEntitiesOfClass(AbstractArrowEntity.class, p_185616_1_.getShape(p_185616_2_, p_185616_3_).bounds().move(p_185616_3_));
		boolean flag = !list.isEmpty();
		boolean flag1 = p_185616_1_.getValue(POWERED);
		if (flag != flag1) {
			p_185616_2_.setBlock(p_185616_3_, p_185616_1_.setValue(POWERED, Boolean.valueOf(flag)), 3);
		}

		if (flag) {
			p_185616_2_.getBlockTicks().scheduleTick(new BlockPos(p_185616_3_), this, this.getPressDuration());
		}

	}

	private int getPressDuration() {
		return 20;
	}

	@Override
	protected void checkTickOnNeighbor(World p_176398_1_, BlockPos p_176398_2_, BlockState p_176398_3_) {
		if (!this.isLocked(p_176398_1_, p_176398_2_, p_176398_3_)) {
			boolean flag = p_176398_3_.getValue(TRIGGERED);
			boolean flag1 = this.shouldTurnOn(p_176398_1_, p_176398_2_, p_176398_3_);
			if (flag != flag1 && !p_176398_1_.getBlockTicks().willTickThisTick(p_176398_2_, this)) {
				TickPriority tickpriority = TickPriority.HIGH;
				if (this.shouldPrioritize(p_176398_1_, p_176398_2_, p_176398_3_)) {
					tickpriority = TickPriority.EXTREMELY_HIGH;
				} else if (flag) {
					tickpriority = TickPriority.VERY_HIGH;
				}

				p_176398_1_.getBlockTicks().scheduleTick(p_176398_2_, this, this.getDelay(p_176398_3_), tickpriority);
			}

		}
	}

	@Override
	protected boolean shouldTurnOn(World p_176404_1_, BlockPos p_176404_2_, BlockState p_176404_3_) {
		return p_176404_1_.hasNeighborSignal(p_176404_2_);
	}

	@Override
	public int getDirectSignal(BlockState p_176211_1_, IBlockReader p_176211_2_, BlockPos p_176211_3_, Direction p_176211_4_) {
		return p_176211_1_.getValue(POWERED) && Direction.UP == p_176211_4_ ? 15 : 0;
	}

	@Override
	public int getSignal(BlockState p_180656_1_, IBlockReader p_180656_2_, BlockPos p_180656_3_, Direction p_180656_4_) {
		return p_180656_1_.getValue(POWERED) ? 15 : 0;
	}

	@Override
	protected int getDelay(BlockState p_196346_1_) {
		return 0;
	}

//	Block behavior
	@Override
	public void onPlace(BlockState p_220082_1_, World p_220082_2_, BlockPos p_220082_3_, BlockState p_220082_4_, boolean p_220082_5_) {
		if (p_220082_4_.is(Blocks.AIR)) {
			p_220082_2_.playSound(null, p_220082_3_, Registries.RUBBER_DUCK_PLACE.get(), SoundCategory.BLOCKS, 0.8f, 1f);
		}
		super.onPlace(p_220082_1_, p_220082_2_, p_220082_3_, p_220082_4_, p_220082_5_);
	}

	@Override
	public void onRemove(BlockState p_196243_1_, World p_196243_2_, BlockPos p_196243_3_, BlockState p_196243_4_, boolean p_196243_5_) {
		if (p_196243_4_.is(Blocks.AIR)) {
			p_196243_2_.playSound(null, p_196243_3_, Registries.RUBBER_DUCK_PLACE.get(), SoundCategory.BLOCKS, 0.8f, 1.2f);
		}
		super.onRemove(p_196243_1_, p_196243_2_, p_196243_3_, p_196243_4_, p_196243_5_);
	}

//	Voxelshape stuff
	VoxelShape SHAPE = Stream.of(
			Block.box(4.6, 0, 5, 11.4, 5, 11),
			Block.box(6, 4, 3, 10, 8, 7),
			Block.box(6.4, 4, 1.4, 9.6, 5, 3)
	).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
	private static Map<Direction, VoxelShape> SHAPES = new HashMap<Direction, VoxelShape>();

	protected void runCalculations(VoxelShape shape) {
		for (Direction direction : Direction.values()) {
			calculateShapes(direction, shape);
		}
	}
	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_,
							   ISelectionContext p_220053_4_) {
		VoxelShape voxel = SHAPES.get(p_220053_1_.getValue(FACING));
		return voxel != null ? voxel : VoxelShapes.block();	//Returns a full block if the voxelShape has an error
	}

	protected static void calculateShapes(Direction to, VoxelShape shape) {
		VoxelShape[] buffer = new VoxelShape[] { shape, VoxelShapes.empty() };

		int times = (to.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
		for (int i = 0; i < times; i++) {
			buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.or(buffer[1],
					VoxelShapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
			buffer[0] = buffer[1];
			buffer[1] = VoxelShapes.empty();
		}
		SHAPES.put(to, buffer[0]);
	}

//	Configs
	private static double onEntitySpawnChance = 0.04;
	public static double getOnEntitySpawnChance(){
		return onEntitySpawnChance;
	}
}