package lemon.evolution.util;

import lemon.engine.event.EventWith;
import lemon.futility.Gate;
import lemon.futility.FObservable;
import lemon.futility.Observable;
import lemon.engine.glfw.GLFWInput;
import lemon.engine.toolbox.Disposable;
import lemon.engine.toolbox.Disposables;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class GatedGLFWGameControls<T> implements GameControls<T, GLFWInput>, Disposable {
	private final GLFWGameControls<T> baseControls;
	private final Map<T, Observable<Boolean>> controls = new HashMap<>();
	private final Gate gate = new Gate();
	private final Disposables disposables = new Disposables();

	public GatedGLFWGameControls(GLFWGameControls<T> baseControls) {
		this.baseControls = baseControls;
	}

	@Override
	public <U> void addCallback(Function<GLFWInput, EventWith<U>> inputEvent, Consumer<? super U> callback) {
		baseControls.addCallback(inputEvent, event -> {
			if (gate.output()) {
				callback.accept(event);
			}
		});
	}

	@Override
	public Observable<Boolean> activated(T control) {
		return controls.computeIfAbsent(control, c -> FObservable.ofAnd(baseControls.activated(control), gate.observableOutput(), disposables::add));
	}

	@Override
	public void dispose() {
		disposables.dispose();
	}

	public Gate gate() {
		return gate;
	}
}
