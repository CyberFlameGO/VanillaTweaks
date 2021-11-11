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
package me.machinemaker.vanillatweaks.modules.survival.workstationhighlights;

import me.machinemaker.vanillatweaks.modules.ModuleMessageService;
import me.machinemaker.vanillatweaks.moonshine.annotation.TextColor;
import net.kyori.adventure.audience.Audience;
import net.kyori.moonshine.annotation.Message;
import net.kyori.moonshine.annotation.Placeholder;

interface MessageService extends ModuleMessageService {

    @TextColor(TextColor.RED)
    @Message("modules.workstation-highlights.no-villager-nearby")
    void noVillagerNearby(Audience audience);

    @TextColor(TextColor.YELLOW)
    @Message("modules.workstation-highlights.none-found")
    void noWorkstationFound(Audience audience);

    @TextColor(TextColor.YELLOW)
    @Message("modules.workstation-highlights.located-at")
    void workstationLocatedAt(Audience audience,
                              @Placeholder @TextColor(TextColor.WHITE) int x,
                              @Placeholder @TextColor(TextColor.WHITE) int y,
                              @Placeholder @TextColor(TextColor.WHITE) int z);
}
