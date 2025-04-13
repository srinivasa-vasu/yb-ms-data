package io.mservice.todo;

import io.mservice.todo.HintStatementInspector.PlannerHint;

import static io.mservice.todo.HintStatementInspector.PlannerHint.NOOP;

public final class QueryHintContext {
	private static final ThreadLocal<PlannerHint> HINT_ENABLED = ThreadLocal.withInitial(() -> NOOP);

	public static void setPlannerHint(PlannerHint hint) {
		HINT_ENABLED.set(hint);
	}

	public static boolean isEnabled() {
		return HINT_ENABLED.get() != null && HINT_ENABLED.get() != NOOP;
	}

	public static void clear() {
		HINT_ENABLED.remove();
	}

	public static PlannerHint getPlannerHint() {
		return HINT_ENABLED.get();
	}
}
