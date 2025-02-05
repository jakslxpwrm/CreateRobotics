package com.workert.robotics.base.registries;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.workert.robotics.Robotics;
import com.workert.robotics.content.computers.computer.ComputerBlockEntity;
import com.workert.robotics.content.computers.computer.ComputerInstance;
import com.workert.robotics.content.computers.computer.ComputerRenderer;
import com.workert.robotics.content.computers.ioblocks.redstonedetector.RedstoneDetectorBlockEntity;
import com.workert.robotics.content.computers.ioblocks.redstoneemitter.RedstoneEmitterBlockEntity;
import com.workert.robotics.content.computers.ioblocks.scanner.ScannerBlockEntity;
import com.workert.robotics.content.computers.ioblocks.scanner.ScannerInstance;
import com.workert.robotics.content.computers.ioblocks.scanner.ScannerRenderer;
import com.workert.robotics.unused.smasher.SmasherBlockEntity;

public class BlockEntityRegistry {
	public static void register() {
	}

	public static final BlockEntityEntry<SmasherBlockEntity> SMASHER_BLOCK_ENTITY = Robotics.REGISTRATE
			.tileEntity("smasher", SmasherBlockEntity::new)
			.validBlock(() -> BlockRegistry.SMASHER.get())
			.register();

	public static final BlockEntityEntry<ComputerBlockEntity> COMPUTER = Robotics.REGISTRATE
			.tileEntity("computer", ComputerBlockEntity::new)
			.instance(() -> ComputerInstance::new)
			.validBlock(BlockRegistry.COMPUTER)
			.renderer(() -> ComputerRenderer::new)
			.register();

	public static final BlockEntityEntry<RedstoneDetectorBlockEntity> REDSTONE_DETECTOR = Robotics.REGISTRATE
			.tileEntity("redstone_detector", RedstoneDetectorBlockEntity::new)
			.validBlock(BlockRegistry.REDSTONE_DETECTOR)
			.register();
	public static final BlockEntityEntry<RedstoneEmitterBlockEntity> REDSTONE_EMITTER = Robotics.REGISTRATE
			.tileEntity("redstone_emitter", RedstoneEmitterBlockEntity::new)
			.validBlock(BlockRegistry.REDSTONE_EMITTER)
			.register();
	public static final BlockEntityEntry<ScannerBlockEntity> SCANNER = Robotics.REGISTRATE
			.tileEntity("scanner", ScannerBlockEntity::new)
			.instance(() -> ScannerInstance::new)
			.validBlock(BlockRegistry.SCANNER)
			.renderer(() -> ScannerRenderer::new)
			.register();
}