
\o cirras.cuws.04_01_01_01.insert_audit_tables.dml.log

-- annual_field_crop table is empty  

\qecho Insert annual_field_detail_audit
WITH t2 AS (
    SELECT annual_field_detail_id, update_user
    FROM annual_field_detail 
)
UPDATE annual_field_detail
SET update_user = t2.update_user
FROM t2
WHERE annual_field_detail.annual_field_detail_id = t2.annual_field_detail_id;


\qecho Insert contracted_field_detail_audit
WITH t2 AS (
    SELECT contracted_field_detail_id, update_user
    FROM contracted_field_detail 
)
UPDATE contracted_field_detail
SET update_user = t2.update_user
FROM t2
WHERE contracted_field_detail.contracted_field_detail_id = t2.contracted_field_detail_id;


\qecho Insert field_audit
WITH t2 AS (
    SELECT field_id, update_user
    FROM field 
)
UPDATE field
SET update_user = t2.update_user
FROM t2
WHERE field.field_id = t2.field_id;


\qecho Insert legal_land_audit
WITH t2 AS (
    SELECT legal_land_id, update_user
    FROM legal_land 
)
UPDATE legal_land
SET update_user = t2.update_user
FROM t2
WHERE legal_land.legal_land_id = t2.legal_land_id;


\qecho Insert legal_land_field_xref_audit
WITH t2 AS (
    SELECT legal_land_id, field_id, update_user
    FROM legal_land_field_xref 
)
UPDATE legal_land_field_xref
SET update_user = t2.update_user
FROM t2
WHERE legal_land_field_xref.legal_land_id = t2.legal_land_id
and legal_land_field_xref.field_id = t2.field_id;


\qecho Insert legal_land_risk_area_xref_audit
WITH t2 AS (
    SELECT legal_land_id, risk_area_id, update_user
    FROM legal_land_risk_area_xref 
)
UPDATE legal_land_risk_area_xref
SET update_user = t2.update_user
FROM t2
WHERE legal_land_risk_area_xref.legal_land_id = t2.legal_land_id
and legal_land_risk_area_xref.risk_area_id = t2.risk_area_id;


\qecho Insert inventory_contract_audit
WITH ico2 AS (
    SELECT inventory_contract_guid, update_user
    FROM inventory_contract 
)
UPDATE inventory_contract
SET update_user = ico2.update_user
FROM ico2
WHERE inventory_contract.inventory_contract_guid = ico2.inventory_contract_guid;


\qecho Insert inventory_contract_commodity_audit
WITH icc2 AS (
    SELECT inventory_contract_commodity_guid, update_user
    FROM inventory_contract_commodity 
)
UPDATE inventory_contract_commodity
SET update_user = icc2.update_user
FROM icc2
WHERE inventory_contract_commodity.inventory_contract_commodity_guid = icc2.inventory_contract_commodity_guid;


\qecho Insert inventory_contract_commodity_berries_audit
WITH iccb2 AS (
    SELECT inventory_contract_commodity_berries_guid, update_user
    FROM inventory_contract_commodity_berries 
)
UPDATE inventory_contract_commodity_berries
SET update_user = iccb2.update_user
FROM iccb2
WHERE inventory_contract_commodity_berries.inventory_contract_commodity_berries_guid = iccb2.inventory_contract_commodity_berries_guid;


\qecho Insert inventory_coverage_total_forage_audit
WITH ictf2 AS (
    SELECT inventory_coverage_total_forage_guid, update_user
    FROM inventory_coverage_total_forage 
)
UPDATE inventory_coverage_total_forage
SET update_user = ictf2.update_user
FROM ictf2
WHERE inventory_coverage_total_forage.inventory_coverage_total_forage_guid = ictf2.inventory_coverage_total_forage_guid;


\qecho Insert inventory_berries_audit
WITH ibe2 AS (
    SELECT inventory_berries_guid, update_user
    FROM inventory_berries 
)
UPDATE inventory_berries
SET update_user = ibe2.update_user
FROM ibe2
WHERE inventory_berries.inventory_berries_guid = ibe2.inventory_berries_guid;


\qecho Insert inventory_field_audit
WITH ifd2 AS (
    SELECT inventory_field_guid, update_user
    FROM inventory_field 
)
UPDATE inventory_field
SET update_user = ifd2.update_user
FROM ifd2
WHERE inventory_field.inventory_field_guid = ifd2.inventory_field_guid;


\qecho Insert inventory_seeded_forage_audit
WITH isf2 AS (
    SELECT inventory_seeded_forage_guid, update_user
    FROM inventory_seeded_forage 
)
UPDATE inventory_seeded_forage
SET update_user = isf2.update_user
FROM isf2
WHERE inventory_seeded_forage.inventory_seeded_forage_guid = isf2.inventory_seeded_forage_guid;


\qecho Insert inventory_seeded_grain_audit
WITH isg2 AS (
    SELECT inventory_seeded_grain_guid, update_user
    FROM inventory_seeded_grain 
)
UPDATE inventory_seeded_grain
SET update_user = isg2.update_user
FROM isg2
WHERE inventory_seeded_grain.inventory_seeded_grain_guid = isg2.inventory_seeded_grain_guid;


\qecho Insert inventory_unseeded_audit
WITH iu2 AS (
    SELECT inventory_unseeded_guid, update_user
    FROM inventory_unseeded 
)
UPDATE inventory_unseeded
SET update_user = iu2.update_user
FROM iu2
WHERE inventory_unseeded.inventory_unseeded_guid = iu2.inventory_unseeded_guid;



\qecho Insert declared_yield_contract_audit
WITH dyc2 AS (
    SELECT declared_yield_contract_guid, update_user
    FROM declared_yield_contract 
)
UPDATE declared_yield_contract
SET update_user = dyc2.update_user
FROM dyc2
WHERE declared_yield_contract.declared_yield_contract_guid = dyc2.declared_yield_contract_guid;


\qecho Insert declared_yield_contract_cmdty_forage_audit
WITH dyccf2 AS (
    SELECT declared_yield_contract_cmdty_forage_guid, update_user
    FROM declared_yield_contract_cmdty_forage 
)
UPDATE declared_yield_contract_cmdty_forage
SET update_user = dyccf2.update_user
FROM dyccf2
WHERE declared_yield_contract_cmdty_forage.declared_yield_contract_cmdty_forage_guid = dyccf2.declared_yield_contract_cmdty_forage_guid;


\qecho Insert declared_yield_contract_commodity_audit
WITH dycc2 AS (
    SELECT declared_yield_contract_commodity_guid, update_user
    FROM declared_yield_contract_commodity 
)
UPDATE declared_yield_contract_commodity
SET update_user = dycc2.update_user
FROM dycc2
WHERE declared_yield_contract_commodity.declared_yield_contract_commodity_guid = dycc2.declared_yield_contract_commodity_guid;


\qecho Insert declared_yield_contract_commodity_berries_audit
WITH dyccb2 AS (
    SELECT declared_yield_contract_commodity_berries_guid, update_user
    FROM declared_yield_contract_commodity_berries 
)
UPDATE declared_yield_contract_commodity_berries
SET update_user = dyccb2.update_user
FROM dyccb2
WHERE declared_yield_contract_commodity_berries.declared_yield_contract_commodity_berries_guid = dyccb2.declared_yield_contract_commodity_berries_guid;


\qecho Insert declared_yield_field_audit
WITH dyf2 AS (
    SELECT declared_yield_field_guid, update_user
    FROM declared_yield_field 
)
UPDATE declared_yield_field
SET update_user = dyf2.update_user
FROM dyf2
WHERE declared_yield_field.declared_yield_field_guid = dyf2.declared_yield_field_guid;


\qecho Insert declared_yield_field_commodity_berries_audit
WITH dyfcb2 AS (
    SELECT declared_yield_field_commodity_berries_guid, update_user
    FROM declared_yield_field_commodity_berries 
)
UPDATE declared_yield_field_commodity_berries
SET update_user = dyfcb2.update_user
FROM dyfcb2
WHERE declared_yield_field_commodity_berries.declared_yield_field_commodity_berries_guid = dyfcb2.declared_yield_field_commodity_berries_guid;


\qecho Insert declared_yield_field_forage_audit
WITH dyff2 AS (
    SELECT declared_yield_field_forage_guid, update_user
    FROM declared_yield_field_forage 
)
UPDATE declared_yield_field_forage
SET update_user = dyff2.update_user
FROM dyff2
WHERE declared_yield_field_forage.declared_yield_field_forage_guid = dyff2.declared_yield_field_forage_guid;


\qecho Insert declared_yield_field_rollup_audit
WITH dyfr2 AS (
    SELECT declared_yield_field_rollup_guid, update_user
    FROM declared_yield_field_rollup 
)
UPDATE declared_yield_field_rollup
SET update_user = dyfr2.update_user
FROM dyfr2
WHERE declared_yield_field_rollup.declared_yield_field_rollup_guid = dyfr2.declared_yield_field_rollup_guid;


\qecho Insert declared_yield_field_rollup_forage_audit
WITH dyfrf2 AS (
    SELECT declared_yield_field_rollup_forage_guid, update_user
    FROM declared_yield_field_rollup_forage 
)
UPDATE declared_yield_field_rollup_forage
SET update_user = dyfrf2.update_user
FROM dyfrf2
WHERE declared_yield_field_rollup_forage.declared_yield_field_rollup_forage_guid = dyfrf2.declared_yield_field_rollup_forage_guid;


\qecho Insert declared_yield_field_variety_berries_audit
WITH dyfvb2 AS (
    SELECT declared_yield_field_variety_berries_guid, update_user
    FROM declared_yield_field_variety_berries 
)
UPDATE declared_yield_field_variety_berries
SET update_user = dyfvb2.update_user
FROM dyfvb2
WHERE declared_yield_field_variety_berries.declared_yield_field_variety_berries_guid = dyfvb2.declared_yield_field_variety_berries_guid;


\qecho Insert verified_yield_amendment_audit
WITH vya2 AS (
    SELECT verified_yield_amendment_guid, update_user
    FROM verified_yield_amendment 
)
UPDATE verified_yield_amendment
SET update_user = vya2.update_user
FROM vya2
WHERE verified_yield_amendment.verified_yield_amendment_guid = vya2.verified_yield_amendment_guid;


\qecho Insert verified_yield_contract_audit
WITH vyc2 AS (
    SELECT verified_yield_contract_guid, update_user
    FROM verified_yield_contract 
)
UPDATE verified_yield_contract
SET update_user = vyc2.update_user
FROM vyc2
WHERE verified_yield_contract.verified_yield_contract_guid = vyc2.verified_yield_contract_guid;


\qecho Insert verified_yield_contract_commodity_audit
WITH vycc2 AS (
    SELECT verified_yield_contract_commodity_guid, update_user
    FROM verified_yield_contract_commodity 
)
UPDATE verified_yield_contract_commodity
SET update_user = vycc2.update_user
FROM vycc2
WHERE verified_yield_contract_commodity.verified_yield_contract_commodity_guid = vycc2.verified_yield_contract_commodity_guid;


\qecho Insert verified_yield_grain_basket_audit
WITH vygb2 AS (
    SELECT verified_yield_grain_basket_guid, update_user
    FROM verified_yield_grain_basket 
)
UPDATE verified_yield_grain_basket
SET update_user = vygb2.update_user
FROM vygb2
WHERE verified_yield_grain_basket.verified_yield_grain_basket_guid = vygb2.verified_yield_grain_basket_guid;


\qecho Insert verified_yield_summary_audit
WITH vys2 AS (
    SELECT verified_yield_summary_guid, update_user
    FROM verified_yield_summary 
)
UPDATE verified_yield_summary
SET update_user = vys2.update_user
FROM vys2
WHERE verified_yield_summary.verified_yield_summary_guid = vys2.verified_yield_summary_guid;


\qecho Insert underwriting_comment_audit
WITH t2 AS (
    SELECT underwriting_comment_guid, update_user
    FROM underwriting_comment 
)
UPDATE underwriting_comment
SET update_user = t2.update_user
FROM t2
WHERE underwriting_comment.underwriting_comment_guid = t2.underwriting_comment_guid;

\o 