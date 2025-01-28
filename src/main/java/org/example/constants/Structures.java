package org.example.constants;

import lombok.Getter;

public enum Structures {
    DESERT_VILLAGE("minecraft:village_desert"),
    PLAINS_VILLAGE("minecraft:village_plains"),
    SAVANNA_VILLAGE("minecraft:village_savanna"),
    SNOWY_VILLAGE("minecraft:village_snowy"),
    TAIGA_VILLAGE("minecraft:village_taiga"),
    ANY_VILLAGE("minecraft:village"),
    DESERT_PYRAMID("minecraft:desert_pyramid"),
    JUNGLE_PYRAMID("minecraft:jungle_pyramid"),
    IGLOO("minecraft:igloo"),
    SWAMP_HUT("minecraft:swamp_hut"),
    STRONGHOLD("minecraft:stronghold"),
    MANSION("minecraft:mansion"),
    MONUMENT("minecraft:monument"),
    SHIPWRECK("minecraft:shipwreck"),
    BURIED_TREASURE("minecraft:buried_treasure"),
    RUINED_PORTAL("minecraft:ruined_portal"),
    RUINS_OCEAN("minecraft:ruins_ocean"),
    MINESHAFT("minecraft:mineshaft"),
    NETHER_FORTRESS("minecraft:fortress"),
    END_CITY("minecraft:end_city"),
    BASTION("minecraft:bastion_remnant"),
    PILLAGER_OUTPOST("minecraft:pillager_outpost"),
    ANCIENT_CITY("minecraft:ancient_city"),
    TRAIL_RUIN("minecraft:trail_ruins"),
    ANY("");
    
    @Getter
    private String value;
    
    Structures(String value) {
        this.value = value;
    }
}