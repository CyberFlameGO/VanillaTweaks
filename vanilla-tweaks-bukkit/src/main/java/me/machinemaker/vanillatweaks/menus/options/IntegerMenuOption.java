/*
 * GNU General Public License v3
 *
 * PaperTweaks, a performant replacement for the VanillaTweaks datapacks.
 *
 * Copyright (C) 2021 Machine_Maker
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.machinemaker.vanillatweaks.menus.options;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Function;
import me.machinemaker.vanillatweaks.menus.parts.Previewable;
import me.machinemaker.vanillatweaks.settings.Setting;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.ClickEvent;
import org.checkerframework.checker.nullness.qual.Nullable;

import static me.machinemaker.vanillatweaks.adventure.Components.join;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class IntegerMenuOption<S> extends MenuOption<Integer, S> implements EditableOption<Integer> {

    protected IntegerMenuOption(final Component label, final Function<S, Integer> typeMapper, final Setting<Integer, ?> setting, final Component extendedDescription, final @Nullable Function<Integer, ClickEvent> previewAction) {
        super(label, typeMapper, setting, extendedDescription, previewAction);
    }

    public static <S> IntegerMenuOption<S> of(final String labelKey, final Function<S, Integer> typeMapper, final Setting<Integer, ?> setting) {
        return new Builder<>(translatable(labelKey), typeMapper, setting).build();
    }

    public static <S> IntegerMenuOption<S> of(final String labelKey, final Setting<Integer, S> setting) {
        return new Builder<>(translatable(labelKey), setting::getOrDefault, setting).build();
    }

    public static <S> Builder<S> builder(final String labelKey, final Function<S, Integer> typeMapper, final Setting<Integer, ?> setting) {
        return new Builder<>(translatable(labelKey), typeMapper, setting);
    }

    public static <S> Builder<S> builder(final String labelKey, final Setting<Integer, S> setting) {
        return new Builder<>(translatable(labelKey), setting::getOrDefault, setting);
    }

    @Override
    public Component build(final S object, final String commandPrefix) {
        final List<Component> components = Lists.newArrayList(this.createClickComponent(this.selected(object), commandPrefix), space());

        this.previewAction().ifPresent(previewAction -> {
            components.add(Previewable.createPreviewComponent(this.label(), previewAction.apply(this.selected(object))));
            components.add(space());
        });

        components.addAll(List.of(
                this.label(),
                space(),
                translatable("commands.config.current-value", GRAY, text(this.selected(object))),
                newline()
        ));

        return join(components.toArray(new ComponentLike[0]));
    }

    @Override
    public Component label() {
        return super.label();
    }

    @Override
    public Component defaultValueDescription() {
        return text(this.setting().defaultValue());
    }

    @Override
    public Component validations() {
        return this.setting().validations();
    }

    public static class Builder<S> extends MenuOption.Builder<Integer, IntegerMenuOption<S>, S, Builder<S>> {

        protected Builder(final Component label, final Function<S, Integer> typeMapper, final Setting<Integer, ?> setting) {
            super(label, typeMapper, setting);
        }

        @Override
        public IntegerMenuOption<S> build() {
            return new IntegerMenuOption<>(
                    this.getLabel(),
                    this.getTypeMapper(),
                    this.getSetting(),
                    this.getExtendedDescription(),
                    this.getPreviewAction()
            );
        }
    }
}
