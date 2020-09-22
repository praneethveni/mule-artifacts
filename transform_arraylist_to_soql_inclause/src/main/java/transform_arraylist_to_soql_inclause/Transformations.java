package transform_arraylist_to_soql_inclause;

import java.util.List;

public final class Transformations {

	public static String codeList(List<String> codesList) {

		String flowvar = "";

		if (codesList != null && codesList.size() > 0) {
			for (String codeL : codesList) {
				if (flowvar == "")
					flowvar = "'" + codeL + "'";
				else
					flowvar = flowvar + ",'" + codeL + "'";
			}
			return "(" + flowvar + ")";
		} else
			return "('')";

	}
}
