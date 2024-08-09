package cs;

/**
 * Assignment Item Class that insert the information inside the JComboBox
 */
class AssignmentItem {
	private String key; // Id of the item (Assignment ID)
	private String value; // Information of the item (Assignment information)

	/**
	 * AssignmentItem constructor
	 * 
	 * @param key   this is the ID of the Assignment
	 * @param value this is the title of the Assignment
	 */
	public AssignmentItem(String key, String value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * Key Getter / Accessor
	 * 
	 * @return key (ID)
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Value Getter / Accessor
	 * 
	 * @return value information of the Assignment
	 */
	public String getValue() {
		return value;
	}

	/**
	 * to String method that show the value information
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return value;
	}
}