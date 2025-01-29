package teddy.minecraftautomation.screen.handlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import teddy.minecraftautomation.blocks.ModBlocks;
import teddy.minecraftautomation.blocks.entity.CrusherBlockEntity;
import teddy.minecraftautomation.network.BlockPosPayload;

public class CrusherScreenHandler extends ScreenHandler {
    private final ScreenHandlerContext context;
    public CrusherBlockEntity blockEntity;

    // Client Constructor
    public CrusherScreenHandler(int syncId, PlayerInventory playerInventory, BlockPosPayload payload) {
        this(syncId, playerInventory, (CrusherBlockEntity) playerInventory.player.getWorld().getBlockEntity(payload.pos()));
    }

    // Main Constructor - (Directly called from server)
    public CrusherScreenHandler(int syncId, PlayerInventory playerInventory, CrusherBlockEntity blockEntity) {
        super(ModScreenHandlers.CRUSHER_SCREEN_HANDLER, syncId);

        this.blockEntity = blockEntity;
        this.context = ScreenHandlerContext.create(blockEntity.getWorld(), blockEntity.getPos());

        addPlayerInventory(playerInventory);
        addCrusherInventory(blockEntity);
    }

    void addCrusherInventory(Inventory inventory) {
        // TODO : Add the crusher's inventory
    }

    private void addPlayerInventory(PlayerInventory playerInv) {
        // Inventory
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInv, 9 + (column + (row * 9)), 8 + (column * 18), 84 + (row * 18)));
            }
        }

        // Hotbar
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInv, column, 8 + (column * 18), 142));
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = getSlot(slotIndex);
        if (slot != null && slot.hasStack()) {
            ItemStack inSlot = slot.getStack();
            newStack = inSlot.copy();

            if (slotIndex == 0) {
                if (!insertItem(inSlot, 0, this.slots.size(), false))
                    return ItemStack.EMPTY;
            } else if (!insertItem(inSlot, 0, 0, true))
                return ItemStack.EMPTY;

            if (inSlot.isEmpty())
                slot.setStack(ItemStack.EMPTY);
            else
                slot.markDirty();
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.CRUSHER);
    }
}
