package io.mservice.todo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.resource.jdbc.spi.StatementInspector;

import static io.mservice.todo.HintStatementInspector.PlannerHint.HASH_OFF;
import static io.mservice.todo.HintStatementInspector.PlannerHint.MERGE_OFF;
import static io.mservice.todo.HintStatementInspector.PlannerHint.NEST_OFF;

public class HintStatementInspector implements StatementInspector {

	private boolean containsSQLComments = false;

	public HintStatementInspector(boolean containsSQLComments) {
		this.containsSQLComments = containsSQLComments;
	}

	enum PlannerHint {
		NEST_OFF("Set (enable_nestloop off)"),
		MERGE_OFF("Set (enable_mergejoin off)"),
		HASH_OFF("Set (enable_hashjoin off)"),
		NOOP("");

		public final String hint;

		PlannerHint(String val) {
			this.hint = val;
		}
	}

	private static final Pattern COMMENT_PATTERN = Pattern.compile("^/\\*\\s*criteria:\\s*(.*?)\\s*\\*/\\s*", Pattern.CASE_INSENSITIVE);

	@Override
	public String inspect(String sql) {
		if (QueryHintContext.isEnabled()) {
			return "/*+ " + QueryHintContext.getPlannerHint().hint + " */ " + sql;
		}
		if (containsSQLComments) {
			Matcher matcher = COMMENT_PATTERN.matcher(sql);
			if (matcher.find()) {
				String key = matcher.group(1).trim();
				String strippedSql = sql.substring(matcher.end());
				return switch (PlannerHint.valueOf(key)) {
					case NEST_OFF -> "/*+ " + NEST_OFF.hint + " */ " + strippedSql;
					case MERGE_OFF -> "/*+ " + MERGE_OFF.hint + " */ " + strippedSql;
					case HASH_OFF -> "/*+ " + HASH_OFF.hint + " */ " + strippedSql;
					default -> strippedSql;
				};
			}
		}
		return sql;
	}
}
