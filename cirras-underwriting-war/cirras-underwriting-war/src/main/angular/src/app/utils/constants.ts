
export const REST_VERSION = 1;

export enum ResourcesRoutes {
    LANDING = "",
    INVENTORY_GRAIN_UNSEEDED = "inventory/grain/unseeded",
    INVENTORY_GRAIN_SEEDED = "inventory/grain/seeded",
    INVENTORY_FORAGE = "inventory/forage",
    DOP_GRAIN = "dop/grain",
    DOP_FORAGE = "dop/forage",
    VERIFIED_YIELD_GRAIN = "verified-yield/grain",
    MAINTAIN_DASHBOARD = "maintain/dashboard",
    MAINTAIN_SEEDING_DEADLINES = "maintain/seeding-deadlines",
    MAINTAIN_GRADE_MODIFIERS = "maintain/grade-modifiers",
    MAINTAIN_FORAGE_VARIETY_INSURABILITY = "maintain/forage-variety-insurability",
    MAINTAIN_YIELD_CONVERSION = "maintain/yield-conversion",
    LAND_LIST = "land-management/land-list",
    LAND_MANAGE = "land-management/manage-land",
    LANDING_PAGE = "landingpage",
    UNAUTHORIZED = "unauthorized",
    ERROR_PAGE = "error",
    SIGN_UP = "sign-up",
    SIGN_OUT = "sign-out-page"
}

export const CODE_TABLE_CACHE = {};

export const INSURANCE_PLAN = {
    "GRAPES"        : 1,  
    "TREEFRUITS"    : 2,  
    "BERRIES"       : 3,   
    "GRAIN"         : 4, 
    "FORAGE"        : 5,
    "VEGETABLES"    : 6,
    "FLOWERS"       : 8
}

export const INSURANCE_NAME = {
    "GRAPES"        : "GRAPES",  
    "TREEFRUITS"    : "TREEFRUITS",  
    "BERRIES"       : "BERRIES",   
    "GRAIN"         : "GRAIN", 
    "FORAGE"        : "FORAGE",
    "VEGETABLES"    : "VEGETABLES",
    "FLOWERS"       : "FLOWERS"
}

export const CROP_COMMODITY_TYPE_CONST = {
    ALL: "",  
    INVENTORY: "INV",  
    YIELD: "YLD",   
    UNDERWRITING: "UW"
}

export const CROP_COMMODITY_UNSPECIFIED = {
    ID: null, 
    NAME: "",
    OTHER_NAME: "OTHER",
};

export const UW_COMMENT_TYPE_CODE = {
    INVENTORY_GENERAL: 'INV',
    DOP_GENERAL: 'DOP'
};

export const LAND_UPDATE_TYPE = {
    NEW_LAND: "NEW_LAND", // to add new legal land and new  field
    ADD_EXISTING_LAND: "ADD_EXISTING_LAND", // to add Existing Legal Land and Existing Field 
    ADD_NEW_FIELD: "ADD_NEW_FIELD", // For Existing Legal Land - New Field
    RENAME_LEGAL_LOCATION: "RENAME_LEGAL_LOCATION",
    REPLACE_LEGAL_LOCATION_EXISTING: "REPLACE_LEGAL_LOCATION_EXISTING", // to replace with Existing Legal Land
    REPLACE_LEGAL_LOCATION_NEW: "REPLACE_LEGAL_LOCATION_NEW",  // to replace with NEW Legal Land 
    REMOVE_FIELD_FROM_POLICY: "REMOVE_FIELD_FROM_POLICY", // to remove field from a policy
    DELETE_FIELD: "DELETE_FIELD" // to delete field from the database
};

export const REPORT_TYPE = [
    {
        code: "INVENTORY",
        description: "Inventory"
    },
    {
        code: "DOP",
        description: "Declaration of Production"
    },
];

export const REPORT_CHOICES = {
    INVENTORY: "INVENTORY",  
    DOP: "DOP",   
}

export const REPORT_SORT_BY = [
    {
        code: "policyNumber",
        description: "Policy Number"
    },
    {
        code: "growerName",
        description: "Grower Name"
    },
];

export const SORT_BY_CHOICES = {
    POLICY_NUMBER: "policyNumber",  
    GROWER_NAME: "growerName",   
}
export const REPORT_TYPE_OPTIONS = [
    {
        code: "unseeded",
        description: "Unseeded Inventory"
    },
    {
        code: "seeded",
        description: "Seeded Inventory"
    },
];

export const BASE_COMMODITIES = ['BARLEY', 'CANOLA', 'FALL RYE', 'FIELD PEA', 'OAT', 'WHEAT', 'WINTER WHEAT']

export const PLANT_DURATION = {
    PERENNIAL : "PERENNIAL",
    ANNUAL : "ANNUAL"
}

export const PRIMARY_REFERENCE_TYPE_CODE = {
    // IDENTIFIER: "IDENTIFIER",
    // LEGAL:      "LEGAL",
    // SHORT:      "SHORT",
    // OTHER:      "OTHER",
    // ADDRESS:    "ADDRESS",
    DEFAULT:    "OTHER"
}

export const PRIMARY_LAND_IDENTIFIER_TYPE_CODE = {
    // PID:    "PID",
    // PIN:    "PIN",
    // IR:     "IR",
    // LINC:   "LINC",
    // OTHER:  "OTHER",
    DEFAULT:    "PID"
}

export const LINK_PLANTING_TYPE = {
    ADD_LINK: "ADD_LINK" , // to add new forage planting to the current field
    REMOVE_LINK: "REMOVE_LINK" // to remove linked planting from the grain policy
}

export const PLANT_INSURABILITY_TYPE_CODE ={
    Establishment1: "E1",
    Establishment2: "E2",
    WinterSurvival1: "W1",
    WinterSurvival2: "W2",
    WinterSurvival3: "W3",
}

export const VERIFIED_YIELD_AMENDMENT_CODE = {
    "APPRAISAL"     : "Appraisal",  
    "ASSESSMENT"    : "Assessment"
}