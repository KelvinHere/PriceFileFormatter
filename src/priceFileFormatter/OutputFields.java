package priceFileFormatter;

public enum OutputFields {
	ABBREV_DESCRIPTION,
	THEIR_SKU,
	OUR_SKU,
	DESCRIPTION,
	EXTRA_DESCRIPTION,
	NET_COST,
	PRICE_1,
	PRICE_2,
	GROUP_1,
	GROUP_2,
	SUPPLIER_CODE,
	VAT_SWITCH,
	THEIR_DESCRIPTION;

	public String lowerCase() {
		return name().toLowerCase();
	}
}
