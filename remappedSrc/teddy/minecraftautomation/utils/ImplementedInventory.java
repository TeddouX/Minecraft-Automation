package teddy.minecraftautomation.utils;

import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.NotNull;

public interface ImplementedInventory extends Inventory {

    DefaultedList<ItemStack> getItems();

    @Override
    default int size() {
        return this.getItems().size();
    }

    @Override
    default boolean isEmpty() {
        return this.getItems().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    @NotNull
    default ItemStack getStack(int index) {
        return this.getItems().get(index);
    }

    @Override
    @NotNull
    default ItemStack removeStack(int index, int amount) {
        ItemStack itemStack = Inventories.splitStack(this.getItems(), index, amount);
        if (!itemStack.isEmpty()) {
            markDirty();
        }

        return itemStack;
    }

    @Override
    @NotNull default ItemStack removeStack(int i) {
        return Inventories.removeStack(getItems(), i);
    }

    @Override
    default void setStack(int i, ItemStack itemStack) {
        getItems().set(i, itemStack);
        if (itemStack.getCount() > getMaxCountPerStack())
            itemStack.setCount(getMaxCountPerStack());
        markDirty();
    }

    @Override
    default void clear() {
        getItems().clear();
    }
}
