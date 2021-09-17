package com.github.salvadormg15.rubber_duck;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

public class RubberDuckBlock extends Block{

	public RubberDuckBlock() {
		super(Properties.of(Material.BAMBOO).strength(0.2f, 0.2f));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
		runCalculations(SHAPE);
	}
	
//	Block behavior
	@Override
	public InteractionResult use(BlockState p_225533_1_, Level p_225533_2_, BlockPos p_225533_3_,
			Player p_225533_4_, InteractionHand p_225533_5_, BlockHitResult p_225533_6_) {
		p_225533_2_.playSound(p_225533_4_, p_225533_3_, Registries.RUBBER_DUCK_USE.get(), SoundSource.BLOCKS, 1.3f, 1f);
		return InteractionResult.SUCCESS;
	}
	@Override
	public void onPlace(BlockState p_220082_1_, Level p_220082_2_, BlockPos p_220082_3_, BlockState p_220082_4_,
			boolean p_220082_5_) {
		p_220082_2_.playSound(null, p_220082_3_, Registries.RUBBER_DUCK_PLACE.get(), SoundSource.BLOCKS, 0.8f, 1f);
	}
	@Override
	public void onRemove(BlockState p_196243_1_, Level p_196243_2_, BlockPos p_196243_3_, BlockState p_196243_4_,
			boolean p_196243_5_) {
		p_196243_2_.playSound(null, p_196243_3_, Registries.RUBBER_DUCK_PLACE.get(), SoundSource.BLOCKS, 0.8f, 1.2f);
	}
	
//	Blockstate stuff
	private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> p_206840_1_) {
		p_206840_1_.add(FACING);
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext p_196258_1_) {
		Direction direction = p_196258_1_.getHorizontalDirection().getOpposite();
		BlockState blockstate = defaultBlockState().setValue(FACING, direction);
		return blockstate;
	}
	
//	Voxelshape stuff
	VoxelShape SHAPE = Stream.of(
			Block.box(4.6, 0, 5, 11.4, 5, 11),
			Block.box(6, 4, 3, 10, 8, 7),
			Block.box(6.4, 4, 1.4, 9.6, 5, 3)
			).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
private static Map<Direction, VoxelShape> SHAPES = new HashMap<Direction, VoxelShape>();
	
	protected void runCalculations(VoxelShape shape) {
	    for (Direction direction : Direction.values()) {
	        calculateShapes(direction, shape);
	    }
	}
	@Override
	public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_,
			CollisionContext p_220053_4_) {
		VoxelShape voxel = SHAPES.get(p_220053_1_.getValue(FACING));
		return voxel != null ? voxel : Shapes.block();	//Returns a full block if the voxelShape has an error
	}
	
	protected static void calculateShapes(Direction to, VoxelShape shape) {
	    VoxelShape[] buffer = new VoxelShape[] { shape, Shapes.empty() };
	 
	    int times = (to.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
	    for (int i = 0; i < times; i++) {
	        buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], 
	        		Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
	        buffer[0] = buffer[1];
	        buffer[1] = Shapes.empty();
	    }	
	    SHAPES.put(to, buffer[0]);
	}
	
//	Configs
	private static double onEntitySpawnChance = 0.04;
	public static double getOnEntitySpawnChance(){
		return onEntitySpawnChance;
	}
}
