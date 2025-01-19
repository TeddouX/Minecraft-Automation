package teddy.minecraftautomation.utils;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ImplementedInventory extends Container {

    NonNullList<ItemStack> getItems();

    @Override
    default int getContainerSize() {
        return this.getItems().size();
    }

    @Override
    default boolean isEmpty() {
        return this.getItems().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    @NotNull
    default ItemStack getItem(int index) {
        return this.getItems().get(index);
    }

    @Override
    @NotNull
    default ItemStack removeItem(int index, int amount) {
        ItemStack itemStack = ContainerHelper.removeItem(this.getItems(), index, amount);
        if (!itemStack.isEmpty()) {
            setChanged();
        }

        return itemStack;
    }

    @Override
    @NotNull default ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(getItems(), i);
    }

    @Override
    default void setItem(int i, ItemStack itemStack) {
        getItems().set(i, itemStack);
        if (itemStack.getCount() > getMaxStackSize())
            itemStack.setCount(getMaxStackSize());
        setChanged();
    }

    @Override
    default void clearContent() {
        getItems().clear();
    }
}
