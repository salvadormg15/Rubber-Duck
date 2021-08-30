package com.github.salvadormg15.rubber_duck;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
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
import net.minecraft.world.World;

public class RubberDuckBlock extends Block{

	public RubberDuckBlock() {
		super(Properties.of(Material.BAMBOO).strength(0.2f, 0.2f));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
		runCalculations(SHAPE);
	}
	
//	Block behavior
	@Override
	public ActionResultType use(BlockState p_225533_1_, World p_225533_2_, BlockPos p_225533_3_,
			PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
		p_225533_2_.playSound(p_225533_4_, p_225533_3_, Registries.RUBBER_DUCK_USE.get(), SoundCategory.BLOCKS, 1.3f, 1f);
		return ActionResultType.SUCCESS;
	}
	@Override
	public void onPlace(BlockState p_220082_1_, World p_220082_2_, BlockPos p_220082_3_, BlockState p_220082_4_,
			boolean p_220082_5_) {
		p_220082_2_.playSound(null, p_220082_3_, Registries.RUBBER_DUCK_PLACE.get(), SoundCategory.BLOCKS, 0.8f, 1f);
	}
	@Override
	public void onRemove(BlockState p_196243_1_, World p_196243_2_, BlockPos p_196243_3_, BlockState p_196243_4_,
			boolean p_196243_5_) {
		p_196243_2_.playSound(null, p_196243_3_, Registries.RUBBER_DUCK_PLACE.get(), SoundCategory.BLOCKS, 0.8f, 1.2f);
	}
	
//	Blockstate stuff
	private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> p_206840_1_) {
		p_206840_1_.add(FACING);
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
	private static double onEntitySpawnChance = 0.02;
	public static double getOnEntitySpawnChance(){
		return onEntitySpawnChance;
	}
}
