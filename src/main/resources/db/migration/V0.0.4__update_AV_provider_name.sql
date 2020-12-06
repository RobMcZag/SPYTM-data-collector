-- We fixed a typo in the name of the enum, so we need to fix the DB too

-- Fix quote table
UPDATE spytm.daily_quote
SET provider = 'ALPHA_VANTAGE'
WHERE provider = 'APLPHA_VANTAGE';

-- Fix metadata table
UPDATE spytm.time_serie_metadata
SET provider = 'ALPHA_VANTAGE'
WHERE provider = 'APLPHA_VANTAGE';