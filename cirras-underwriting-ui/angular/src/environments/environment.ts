// This file can be replaced during build by using the `fileReplacements` array.
// `ng build ---prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
    production: false,
    app_config_location: "dev-config/appConfig.json",
    override_sign_up: false,
    restrict_imports: false,
    staff_supplier_invs_location: "assets/stub-data/staff-supplier-invs-data.json",
    supplier_bulk_managment_location: "assets/stub-data/supplier-bulk-management-data.json",
    user_access_invitations_location: "assets/stub-data/user-access-invitations-data.json",
};

/*
 * In development mode, to ignore zone related error stack frames such as
 * `zone.run`, `zoneDelegate.invokeTask` for easier debugging, you can
 * import the following file, but please comment it out in production mode
 * because it will have performance impact when throw error
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
