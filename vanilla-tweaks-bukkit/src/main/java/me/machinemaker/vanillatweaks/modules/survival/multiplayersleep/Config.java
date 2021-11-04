/*
 * GNU General Public License v3
 *
 * VanillaTweaks, a performant replacement for the VanillaTweaks datapacks.
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
package me.machinemaker.vanillatweaks.modules.survival.multiplayersleep;

import cloud.commandframework.context.CommandContext;
import me.machinemaker.lectern.annotations.Description;
import me.machinemaker.lectern.annotations.Key;
import me.machinemaker.vanillatweaks.cloud.dispatchers.CommandDispatcher;
import me.machinemaker.vanillatweaks.cloud.dispatchers.PlayerCommandDispatcher;
import me.machinemaker.vanillatweaks.config.I18nKey;
import me.machinemaker.vanillatweaks.config.VTConfig;
import me.machinemaker.vanillatweaks.menus.Menu;
import me.machinemaker.vanillatweaks.menus.MergedMenus;
import me.machinemaker.vanillatweaks.menus.ReferenceConfigurationMenu;
import me.machinemaker.vanillatweaks.menus.options.EnumMenuOption;
import me.machinemaker.vanillatweaks.menus.options.IntegerMenuOption;
import me.machinemaker.vanillatweaks.menus.options.MenuOption;
import me.machinemaker.vanillatweaks.menus.parts.MenuPartLike;
import me.machinemaker.vanillatweaks.modules.MenuModuleConfig;
import me.machinemaker.vanillatweaks.settings.types.GameRuleSetting;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static net.kyori.adventure.text.Component.text;

@VTConfig
@Menu(commandPrefix = "/multiplayersleep admin config")
class Config extends MenuModuleConfig<Config, MergedMenus.Menu1<Config, World>> {

    private static final Map<BossBar.Color, TextColor> COLOR_MAP = Map.of(
            BossBar.Color.PINK, TextColor.color(0xec00b8),
            BossBar.Color.BLUE, TextColor.color(0x00b7ec),
            BossBar.Color.RED, TextColor.color(0xec3500),
            BossBar.Color.GREEN, TextColor.color(0x1dec00),
            BossBar.Color.YELLOW, TextColor.color(0xe9ec00),
            BossBar.Color.PURPLE, TextColor.color(0x7b00ec),
            BossBar.Color.WHITE, TextColor.color(0xececec)
    );

    @Key("included-worlds")
    @Description("Worlds to count player's from")
    private List<World> includedWorlds = List.of(Bukkit.getWorlds().get(0));

    @Key("defaults.display")
    @I18nKey("modules.multiplayer-sleep.settings.default-display")
    @Description("Default display for new players")
    public Settings.DisplaySetting defaultDisplaySetting = Settings.DisplaySetting.CHAT;

    @Key("boss-bar-color")
    @I18nKey("modules.multiplayer-sleep.settings.boss-bar-color")
    @Description("modules.multiplayer-sleep.settings.boss-bar-color.extended")
    public BossBar.Color bossBarColor = BossBar.Color.WHITE;

    @Key("always-reset-weather-cycle")
    @I18nKey("modules.multiplayer-sleep.settings.always-reset-weather-cycle")
    @Description("modules.multiplayer-sleep.settings.always-reset-weather-cycle.extended")
    public boolean alwaysResetWeatherCycle = false;

    public List<World> worlds(boolean log) {
        for (World world : this.includedWorlds) {
            if (log && !Boolean.TRUE.equals(world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE))) {
                MultiplayerSleep.LOGGER.warn("{} does not have the gamerule doDaylightCycle set to true, passing the night will have no effect there.", world.getName());
            }
        }
        if (this.includedWorlds.isEmpty()) {
            MultiplayerSleep.LOGGER.warn("You haven't enabled any worlds to be tracked by the MultiplayerSleep module");
        }
        return this.includedWorlds;
    }

    @Override
    protected MergedMenus.@NotNull Menu1<Config, World> createMenu(@NotNull Component title, @NotNull String commandPrefix, @NotNull List<MenuPartLike<Config>> configMenuParts) {
        return new MergedMenus.Menu1<>(
                new ReferenceConfigurationMenu<>(title, commandPrefix, configMenuParts, this),
                IntegerMenuOption.builder("gamerule.playersSleepingPercentage", GameRuleSetting.ofInt(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 0, 100)).extendedDescription("gamerule.playersSleepingPercentage.description").configure("/gamerule")
                );
    }

    @Override
    protected void sendMenu(@NotNull CommandContext<CommandDispatcher> context) {
        Player player = PlayerCommandDispatcher.from(context);
        context.getSender().sendMessage(this.menu().build(this, player.getWorld()));
    }

    @Override
    protected @NotNull MenuOption.Builder<?, ?, Config, ?> touchMenuOption(MenuOption.@NotNull Builder<?, ?, Config, ?> optionBuilder) {
        if (optionBuilder instanceof EnumMenuOption.Builder<?, ?> configBuilder && configBuilder.getSetting().valueType() == BossBar.Color.class) {
            configBuilder.optionLabelFunction((color) -> {
                return text(Objects.requireNonNull(BossBar.Color.NAMES.key((BossBar.Color) color)), COLOR_MAP.get((BossBar.Color) color), TextDecoration.BOLD);
            });
        }
        return optionBuilder;
    }

    @Override
    public @NotNull Component title() {
        return buildDefaultTitle("Multiplayer Sleep");
    }
}
