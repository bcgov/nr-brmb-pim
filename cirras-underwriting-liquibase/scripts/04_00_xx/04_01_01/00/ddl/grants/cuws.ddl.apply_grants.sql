
GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.declared_yield_field_commodity_berries TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.declared_yield_field_variety_berries TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.declared_yield_contract_commodity_berries TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.commodity_maturity_scale TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.declared_yield_contract_commodity_berries_ob TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.claim_calculation_berries_sync TO "app_cuws_rest_proxy";

-- Grant permissios to the audit tables
GRANT SELECT, INSERT ON cuws.annual_field_crop_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.annual_field_detail_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.audit_transaction_type_code TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.contracted_field_detail_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.declared_yield_contract_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.declared_yield_contract_cmdty_forage_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.declared_yield_contract_commodity_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.declared_yield_contract_commodity_berries_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.declared_yield_field_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.declared_yield_field_commodity_berries_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.declared_yield_field_forage_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.declared_yield_field_rollup_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.declared_yield_field_rollup_forage_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.declared_yield_field_variety_berries_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.field_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.inventory_berries_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.inventory_contract_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.inventory_contract_commodity_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.inventory_contract_commodity_berries_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.inventory_coverage_total_forage_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.inventory_field_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.inventory_seeded_forage_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.inventory_seeded_grain_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.inventory_unseeded_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.legal_land_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.legal_land_field_xref_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.legal_land_risk_area_xref_audit TO "app_cuws_rest_proxy"; 
GRANT SELECT, INSERT ON cuws.underwriting_comment_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.verified_yield_amendment_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.verified_yield_contract_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.verified_yield_contract_commodity_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.verified_yield_grain_basket_audit TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT ON cuws.verified_yield_summary_audit TO "app_cuws_rest_proxy";


--Grant access to sequences to proxy
GRANT USAGE ON ALL SEQUENCES IN SCHEMA cuws TO "app_cuws_rest_proxy";

--Grant read only access to all tables for the readonly role
GRANT SELECT ON ALL TABLES IN SCHEMA cuws TO "app_cuws_readonly";
