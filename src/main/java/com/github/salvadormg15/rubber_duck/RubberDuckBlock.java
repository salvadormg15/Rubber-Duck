package com.github.salvadormg15.rubber_duck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class RubberDuckBlock extends Block{
	public RubberDuckBlock() {
		super(Properties.of(Material.BAMBOO).strength(0.2f, 0.2f));
		this.registerDefaultState(
			this.stateDefinition.any()
			.setValue(FACING, Direction.NORTH)
			.setValue(POWERED, Boolean.valueOf(false))
			.setValue(DISARMED, Boolean.valueOf(false))
		);
		runCalculations(SHAPE);
	}

//  Redstone behavior - Taken and modified from AbstractButtonBlock
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty DISARMED = BlockStateProperties.DISARMED;

	private final boolean sensitive = false;

	private int getPressDuration() {
		return this.sensitive ? 30 : 20;
	}

	@Override
	public ActionResultType use(BlockState p_225533_1_, World p_225533_2_, BlockPos p_225533_3_, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
		this.press(p_225533_1_, p_225533_2_, p_225533_3_);
		p_225533_2_.playSound(p_225533_4_, p_225533_3_, Registries.RUBBER_DUCK_USE.get(), SoundCategory.BLOCKS, 1.3f, 1f);
		return ActionResultType.sidedSuccess(p_225533_2_.isClientSide);
	}

	public void press(BlockState p_226910_1_, World p_226910_2_, BlockPos p_226910_3_) {
		p_226910_2_.setBlock(p_226910_3_, p_226910_1_.setValue(POWERED, Boolean.valueOf(true)).setValue(DISARMED, Boolean.valueOf(true)), 3);
		this.updateNeighbours(p_226910_1_, p_226910_2_, p_226910_3_);
		p_226910_2_.getBlockTicks().scheduleTick(p_226910_3_, this, this.getPressDuration());
	}

	@Override
	public void onRemove(BlockState p_196243_1_, World p_196243_2_, BlockPos p_196243_3_, BlockState p_196243_4_, boolean p_196243_5_) {
		if (!p_196243_5_ && !p_196243_1_.is(p_196243_4_.getBlock())) {
			if (p_196243_1_.getValue(POWERED)) {
				this.updateNeighbours(p_196243_1_, p_196243_2_, p_196243_3_);
			}

			super.onRemove(p_196243_1_, p_196243_2_, p_196243_3_, p_196243_4_, p_196243_5_);
			p_196243_2_.playSound(null, p_196243_3_, Registries.RUBBER_DUCK_PLACE.get(), SoundCategory.BLOCKS, 0.8f, 1.2f);
		}
	}

	@Override
	public int getSignal(BlockState p_180656_1_, IBlockReader p_180656_2_, BlockPos p_180656_3_, Direction p_180656_4_) {
		return p_180656_1_.getValue(POWERED) ? 15 : 0;
	}

	@Override
	public int getDirectSignal(BlockState p_176211_1_, IBlockReader p_176211_2_, BlockPos p_176211_3_, Direction p_176211_4_) {
		return p_176211_1_.getValue(POWERED) && Direction.UP == p_176211_4_ ? 15 : 0;
	}

	@Override
	public boolean isSignalSource(BlockState p_149744_1_) {
		return true;
	}

	@Override
	public void tick(BlockState p_225534_1_, ServerWorld p_225534_2_, BlockPos p_225534_3_, Random p_225534_4_) {
		if (p_225534_1_.getValue(POWERED)) {
			this.checkPressed(p_225534_1_, p_225534_2_, p_225534_3_);
		}
		if (p_225534_1_.getValue(DISARMED) && !p_225534_2_.hasNeighborSignal(p_225534_3_)) {
			p_225534_2_.setBlock(p_225534_3_, p_225534_1_.setValue(DISARMED, false), 2);
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
			this.updateNeighbours(p_185616_1_, p_185616_2_, p_185616_3_);
		}

		if (flag) {
			p_185616_2_.getBlockTicks().scheduleTick(new BlockPos(p_185616_3_), this, this.getPressDuration());
		}

	}

	private void updateNeighbours(BlockState p_196368_1_, World p_196368_2_, BlockPos p_196368_3_) {
		p_196368_2_.updateNeighborsAt(p_196368_3_, this);
		p_196368_2_.updateNeighborsAt(p_196368_3_.relative(Direction.DOWN), this);
	}

	@Override
	public void neighborChanged(BlockState p_220069_1_, World p_220069_2_, BlockPos p_220069_3_, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {
		if (!p_220069_2_.isClientSide) {
			boolean flag = p_220069_1_.getValue(DISARMED);
			if (flag != p_220069_2_.hasNeighborSignal(p_220069_3_)) {
				if (flag) {
					p_220069_2_.getBlockTicks().scheduleTick(p_220069_3_, this, 0);
				} else {
					p_220069_2_.playSound(null, p_220069_3_, Registries.RUBBER_DUCK_USE.get(), SoundCategory.BLOCKS, 1.3f, 1f);
					p_220069_2_.setBlock(p_220069_3_, p_220069_1_.cycle(DISARMED), 2);
				}
			}
		}
	}

	//	Block behavior
	@Override
	public void onPlace(BlockState p_220082_1_, World p_220082_2_, BlockPos p_220082_3_, BlockState p_220082_4_,
			boolean p_220082_5_) {
		if (p_220082_4_.is(Blocks.AIR)) {
			p_220082_2_.playSound(null, p_220082_3_, Registries.RUBBER_DUCK_PLACE.get(), SoundCategory.BLOCKS, 0.8f, 1f);
		}
	}

//	Blockstate stuff
	private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> p_206840_1_) {
		p_206840_1_.add(FACING, POWERED, DISARMED);
	}
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
		Direction direction = p_196258_1_.getHorizontalDirection().getOpposite();
		BlockState blockstate = defaultBlockState().setValue(FACING, direction);
		return blockstate;
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
