package teddy.minecraftautomation.blocks.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import teddy.minecraftautomation.network.BlockPosPayload;
import teddy.minecraftautomation.screen.handlers.CrusherScreenHandler;
import teddy.minecraftautomation.utils.ContainerUtils;
import teddy.minecraftautomation.utils.ImplementedFluidStorage;
import teddy.minecraftautomation.utils.ImplementedInventory;

public class CrusherBlockEntity extends BlockEntity implements ImplementedInventory, ImplementedFluidStorage, ExtendedScreenHandlerFactory<BlockPosPayload> {
    private static final long FLUID_STORAGE_SIZE_MB = ContainerUtils.convertMbToDroplets(2000);
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    public SingleVariantStorage<FluidVariant> fluidStorage;

    public CrusherBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRUSHER_BE, pos, state);

        this.fluidStorage = initializeFluidStorage(FLUID_STORAGE_SIZE_MB, this::markDirty);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);

        Inventories.writeNbt(nbt, getItems(), registries);
        writeFluidStorageNbt(nbt, registries);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        Inventories.readNbt(nbt, getItems(), registries);
        this.fluidStorage = readFluidStorageNbt(nbt, registries, this::markDirty);

        super.readNbt(nbt, registries);
    }

    @Override
    public void markDirty() {
        super.markDirty();

        World world = this.getWorld();

        if (world == null)
            return;

        world.updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        NbtCompound nbt = new NbtCompound();

        this.writeNbt(nbt, registries);

        return nbt;
    }

    @Override
    public SingleVariantStorage<FluidVariant> getFluidStorage() {
        return this.fluidStorage;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public BlockPosPayload getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return new BlockPosPayload(this.getPos());
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(this.getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CrusherScreenHandler(syncId, playerInventory, this);
    }
}
