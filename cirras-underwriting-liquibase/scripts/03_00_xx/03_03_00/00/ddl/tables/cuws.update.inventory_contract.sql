-- make grain_from_prev_year_ind nullable
ALTER TABLE cuws.inventory_contract ALTER COLUMN grain_from_prev_year_ind DROP NOT NULL;