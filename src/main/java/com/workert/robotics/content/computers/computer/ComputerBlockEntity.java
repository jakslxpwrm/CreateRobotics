package com.workert.robotics.content.computers.computer;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.workert.robotics.base.roboscript.RoboScript;
import com.workert.robotics.base.roboscript.RoboScriptCallable;
import com.workert.robotics.base.roboscript.ingame.LineLimitedString;
import com.workert.robotics.base.roboscript.util.RoboScriptArgumentPredicates;
import com.workert.robotics.base.roboscript.util.RoboScriptObjectConversions;
import com.workert.robotics.content.computers.ioblocks.IOBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComputerBlockEntity extends KineticTileEntity {
	public final RoboScript roboScript;
	private String script = "";

	private static final int TERMINAL_LINE_LIMIT = 2048;
	private LineLimitedString terminal = new LineLimitedString(TERMINAL_LINE_LIMIT);

	private boolean running = false;

	private List<String> outputDisplay = new ArrayList<>();

	public Map<String, BlockPos> connectedBlocks = new HashMap<>();


	public ComputerBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);

		this.roboScript = new RoboScript() {
			@Override
			protected void handlePrintMessage(String message) {
				ComputerBlockEntity.this.terminal.addLine(message);
				ComputerBlockEntity.this.notifyUpdate();
			}

			@Override
			protected void handleErrorMessage(String error) {
				ComputerBlockEntity.this.terminal.addLine(error);
				ComputerBlockEntity.this.notifyUpdate();
			}

			@Override
			protected void defineNativeFunctions() {
				super.defineNativeFunctions();
				this.defineNativeFunction("display", 1, (args) -> {
					if (args[0] instanceof List l) {
						ComputerBlockEntity.this.outputDisplay = RoboScriptObjectConversions.stringifyAllElements(l);
						return null;
					}
					ComputerBlockEntity.this.outputDisplay = List.of(RoboScriptObjectConversions.stringify(args[0]));
					return null;
				});
				this.defineNativeFunction("getConnectedBlock", 1, (args) -> {
					String signalName = RoboScriptArgumentPredicates.asString(args[0]);
					BlockPos pos = ComputerBlockEntity.this.connectedBlocks.get(signalName);
					if (pos == null) return null;
					if (ComputerBlockEntity.this.getLevel().getExistingBlockEntity(pos) instanceof IOBlockEntity be) {
						return be.getRoboScriptObject();
					}
					return null;
				});
			}
		};
	}


	public void runScript() {
		this.roboScript.runString(this.script);
		this.running = true;
	}

	public void turnOff() {
		this.roboScript.queueStopForProgram();
		this.running = false;
	}

	public void interpretSignal(RoboScriptCallable callable, Object[] args) {
		if (this.running)
			this.roboScript.queueSignal(callable, args);
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		this.script = compound.getString("Script");
		this.terminal = new LineLimitedString(TERMINAL_LINE_LIMIT, compound.getString("Terminal"));
		this.running = compound.getBoolean("Running");
		this.connectedBlocks = getConnectedBlocksFromTag(compound.getCompound("ConnectedBlocks"));
	}

	@Override
	protected void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		compound.putString("Script", this.script);
		compound.putString("Terminal", this.terminal.getString());
		compound.putBoolean("Running", this.running);
		compound.put("ConnectedBlocks", getConnectedBlocksToTag(this.connectedBlocks));
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.isSpeedRequirementFulfilled() && this.running) {
			this.roboScript.queueStopForProgram();
			this.terminal.addLine("ERROR: Speed requirement not fulfilled, stopped program");
			this.running = false;

		}
	}

	public LineLimitedString getTerminal() {
		return this.terminal;
	}

	public String getScript() {
		return this.script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public boolean getRunning() {
		return this.running;
	}

	public List<String> getOutputDisplay() {
		return this.outputDisplay;
	}

	public void clearTerminal() {
		this.terminal = new LineLimitedString(TERMINAL_LINE_LIMIT);
	}


	private static List<String> getOutputDisplayFromTag(ListTag listTag) {
		List<String> stringList = new ArrayList<>();
		for (Object o : listTag.toArray()) {
			if (!(o instanceof String s)) return null;
			stringList.add(s);
		}
		return stringList;
	}

	private static ListTag getOutputDisplayToTag(List<String> stringList) {
		ListTag listTag = new ListTag();
		for (String s : stringList) {
			listTag.add(StringTag.valueOf(s));
		}
		return listTag;
	}

	private static CompoundTag getConnectedBlocksToTag(Map<String, BlockPos> connectedBlocks) {
		CompoundTag compoundTag = new CompoundTag();
		for (Map.Entry<String, BlockPos> entry : connectedBlocks.entrySet()) {
			compoundTag.put(entry.getKey(), NbtUtils.writeBlockPos(entry.getValue()));
		}
		return compoundTag;
	}

	private static Map<String, BlockPos> getConnectedBlocksFromTag(CompoundTag compoundTag) {
		Map<String, BlockPos> connectedBlocks = new HashMap<>();
		for (String key : compoundTag.getAllKeys()) {
			connectedBlocks.put(key, NbtUtils.readBlockPos(compoundTag.getCompound(key)));
		}
		return connectedBlocks;
	}
}
