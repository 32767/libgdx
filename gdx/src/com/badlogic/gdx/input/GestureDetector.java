/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * {@link InputProcessor} implementation that detects gestures (tap, long press, fling,
 * pan, zoom, pinch) and hands them to a {@link GestureListener}.
 * @author mzechner
 *
 */
public class GestureDetector extends InputAdapter {
	/**
	 * Register an instance of this class with a {@link GestureDetector} to 
	 * receive gestures such as taps, long presses, flings, panning or pinch
	 * zooming. Each method returns a boolean indicating if the event should
	 * be handed to the next listener (false to hand it to the next listener, true
	 * otherwise).
	 * @author mzechner
	 *
	 */
	public static interface GestureListener {
		/**
		 * Called when a finger went down on the screen or a mouse button was
		 * pressed.
		 * @param x 
		 * @param y
		 * @param pointer
		 * @return
		 */
		public boolean touchDown (int x, int y, int pointer);

		/**
		 * Called when a tap occured. A tap happens if a finger went down on 
		 * the screen and was lifted again without moving outside of the tap square.
		 * The tap square is a rectangular area around the initial touch position
		 * as specified on construction time of the {@link GestureDetector}. 
		 * @param x
		 * @param y
		 * @param count the number of taps. 
		 * @return
		 */
		public boolean tap (int x, int y, int count);

		public boolean longPress (int x, int y);

		/**
		 * Called when the user dragged a finger over the screen and lifted it. Reports
		 * the last known velocity of the finger in pixels per second.
		 * @param velocityX velocity on x in seconds
		 * @param velocityY velocity on y in seconds
		 * @return
		 */
		public boolean fling (float velocityX, float velocityY);

		/**
		 * Called when the user drags a finger over the screen.
		 * @param x
		 * @param y
		 * @param deltaX the difference in pixels to the last drag event on x.
		 * @param deltaY the difference in pixels to the last drag event on y.
		 * @return
		 */
		public boolean pan (int x, int y, int deltaX, int deltaY);

		/**
		 * Called when the user performs a pinch zoom gesture. The original distance
		 * is the distance in pixels when the gesture started.
		 * @param originalDistance distance between fingers when the gesture started.
		 * @param currentDistance current distance between fingers.
		 * @return
		 */
		public boolean zoom (float originalDistance, float currentDistance);

		/**
		 * Called when a user performs a pinch zoom gesture. Reports the initial positions
		 * of the two involved fingers and their current positions.
		 * @param initialFirstPointer
		 * @param initialSecondPointer
		 * @param firstPointer
		 * @param secondPointer
		 * @return
		 */
		public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer);
	}

	/**
	 * Derrive from this if you only want to implement a subset of {@link GestureListener}.
	 * @author mzechner
	 *
	 */
	public static class GestureAdapter implements GestureListener {
		public boolean touchDown (int x, int y, int pointer) {
			return false;
		}

		public boolean tap (int x, int y, int count) {
			return false;
		}

		public boolean longPress (int x, int y) {
			return false;
		}

		public boolean fling (float velocityX, float velocityY) {
			return false;
		}

		public boolean pan (int x, int y, int deltaX, int deltaY) {
			return false;
		}

		public boolean zoom (float originalDistance, float currentDistance) {
			return false;
		}

		public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer) {
			return false;
		}
	}

	static class VelocityTracker {
		int sampleSize = 10;
		int lastX;
		int lastY;
		int deltaX;
		int deltaY;
		long lastTime;
		int numSamples;
		float[] meanX = new float[sampleSize];
		float[] meanY = new float[sampleSize];
		long[] meanTime = new long[sampleSize];

		public void start (int x, int y, long timeStamp) {
			lastX = x;
			lastY = y;
			deltaX = 0;
			deltaY = 0;
			numSamples = 0;
			for (int i = 0; i < sampleSize; i++) {
				meanX[i] = 0;
				meanY[i] = 0;
				meanTime[i] = 0;
			}
			lastTime = timeStamp;
		}

		public void update (int x, int y, long timeStamp) {
			long currTime = timeStamp;
			deltaX = (x - lastX);
			deltaY = (y - lastY);
			lastX = x;
			lastY = y;
			long deltaTime = currTime - lastTime;
			lastTime = currTime;
			int index = numSamples % sampleSize;
			meanX[index] = deltaX;
			meanY[index] = deltaY;
			meanTime[index] = deltaTime;
			numSamples++;
		}

		public float getVelocityX () {
			float meanX = getAverage(this.meanX, numSamples);
			float meanTime = getAverage(this.meanTime, numSamples) / 1000000000.0f;
			if (meanTime == 0) return 0;
			return meanX / meanTime;
		}

		public float getVelocityY () {
			float meanY = getAverage(this.meanY, numSamples);
			float meanTime = getAverage(this.meanTime, numSamples) / 1000000000.0f;
			if (meanTime == 0) return 0;
			return meanY / meanTime;
		}

		private float getAverage (float[] values, int numSamples) {
			numSamples = Math.min(sampleSize, numSamples);
			float sum = 0;
			for (int i = 0; i < numSamples; i++) {
				sum += values[i];
			}
			return sum / numSamples;
		}

		private long getAverage (long[] values, int numSamples) {
			numSamples = Math.min(sampleSize, numSamples);
			long sum = 0;
			for (int i = 0; i < numSamples; i++) {
				sum += values[i];
			}
			if (numSamples == 0) return 0;
			return sum / numSamples;
		}

		private float getSum (float[] values, int numSamples) {
			numSamples = Math.min(sampleSize, numSamples);
			float sum = 0;
			for (int i = 0; i < numSamples; i++) {
				sum += values[i];
			}
			if (numSamples == 0) return 0;
			return sum;
		}
	}

	private final int tapSquareSize;
	private final long tapCountInterval;
	private final long longPressDuration;
	private long maxFlingDelay;
	private boolean inTapSquare;
	private int tapCount;
	private long lastTapTime;
	private boolean longPressFired;
	private boolean pinching;
	private boolean panning;

	private final VelocityTracker tracker = new VelocityTracker();
	private int tapSquareCenterX;
	private int tapSquareCenterY;
	private long gestureStartTime;
	private Vector2 firstPointer = new Vector2();
	private Vector2 secondPointer = new Vector2();
	private Vector2 initialFirstPointer = new Vector2();
	private Vector2 initialSecondPointer = new Vector2();

	private final GestureListener listener;

	/**
	 * Creates a new GestureDetector that will pass on all gestures
	 * to the specified {@link GestureListener}
	 * @param listener
	 */
	public GestureDetector (GestureListener listener) {
		this(20, 0.4f, 1.5f, 0.15f, listener);
	}

	/**
	 * Creates a new GestureDetector that will pass on all gestures
	 * to the specified {@link GestureListener}
	 * @param halfTapSquareSize half width in pixels of the square around an initial touch event, see {@link GestureListener#tap(int, int, int)} 
	 * @param tapCountInterval time in seconds that must pass for two touch down/up sequences to be detected as consecutive taps.
	 * @param longPressDuration time in seconds that must pass for the detector to fire a {@link GestureListener#longPress(int, int)} event.
	 * @param maxFlingDelay time in seconds the finger must have been dragged for a fling event to be fired, see {@link GestureListener#fling(float, float)}
	 * @param listener
	 */
	public GestureDetector (int halfTapSquareSize, float tapCountInterval, float longPressDuration, float maxFlingDelay,
		GestureListener listener) {
		this.tapSquareSize = halfTapSquareSize;
		this.tapCountInterval = (long)(tapCountInterval * 1000000000l);
		this.longPressDuration = (long)(longPressDuration * 1000000000l);
		this.maxFlingDelay = (long)(maxFlingDelay * 1000000000l);
		this.listener = listener;
	}

	@Override
	public boolean touchDown (int x, int y, int pointer, int button) {
		if (pointer > 1) return false;

		if (pointer == 0) {
			firstPointer.set(x, y);
			gestureStartTime = Gdx.input.getCurrentEventTime();
			tracker.start(x, y, gestureStartTime);
			// we are still touching with the second finger -> pinch mode
			if (Gdx.input.isTouched(1)) {
				inTapSquare = false;
				pinching = true;
				initialFirstPointer.set(firstPointer);
				initialSecondPointer.set(secondPointer);
			} else {
				inTapSquare = true;
				pinching = false;
				longPressFired = false;
				tapSquareCenterX = x;
				tapSquareCenterY = y;
			}
		} else {
			secondPointer.set(x, y);
			inTapSquare = false;
			pinching = true;
			initialFirstPointer.set(firstPointer);
			initialSecondPointer.set(secondPointer);
		}
		return listener.touchDown(x, y, pointer);
	}

	@Override
	public boolean touchDragged (int x, int y, int pointer) {
		if (pointer > 1) return false;

		// handle pinch zoom
		if (pinching) {
			if (pointer == 0)
				firstPointer.set(x, y);
			else
				secondPointer.set(x, y);
			if (listener != null) {
				boolean result = listener.pinch(initialFirstPointer, initialSecondPointer, firstPointer, secondPointer);
				return listener.zoom(initialFirstPointer.dst(initialSecondPointer), firstPointer.dst(secondPointer)) || result;
			}
			return false;
		}

		// update tracker
		tracker.update(x, y, Gdx.input.getCurrentEventTime());

		// check if we are still tapping.
		if (!(inTapSquare && Math.abs(x - tapSquareCenterX) < tapSquareSize && Math.abs(y - tapSquareCenterY) < tapSquareSize)) {
			inTapSquare = false;
		}

		if (!inTapSquare) {
			// handle scroll
			inTapSquare = false;
			panning = true;
			return listener.pan(tracker.lastX, tracker.lastY, tracker.deltaX, tracker.deltaY);
		} else {
			// handle longpress
			if (!longPressFired && Gdx.input.getCurrentEventTime() - gestureStartTime > longPressDuration) {
				longPressFired = true;
				return listener.longPress(x, y);
			}
		}

		return false;
	}

	@Override
	public boolean touchUp (int x, int y, int pointer, int button) {
		if (pointer > 1) return false;

		panning = false;
		if (inTapSquare & !longPressFired) {
			// handle taps
			if (TimeUtils.nanoTime() - lastTapTime > tapCountInterval) tapCount = 0;
			tapCount++;
			lastTapTime = TimeUtils.nanoTime();
			gestureStartTime = 0;
			return listener.tap(tapSquareCenterX, tapSquareCenterY, tapCount);
		} else if (pinching) {
			// handle pinch end
			pinching = false;
			panning = true;
			// we are basically in pan/scroll mode again, reset velocity tracker
			if (pointer == 0) {
				// first pointer has lifted off, set up panning to use the second pointer...
				tracker.start((int)secondPointer.x, (int)secondPointer.y, Gdx.input.getCurrentEventTime());
			} else {
				// second pointer has lifted off, set up panning to use the first pointer...
				tracker.start((int)firstPointer.x, (int)firstPointer.y, Gdx.input.getCurrentEventTime());
			}
		} else {
			gestureStartTime = 0;
			// handle fling
			long time = Gdx.input.getCurrentEventTime();
			if (time - tracker.lastTime < maxFlingDelay) {
				tracker.update(x, y, time);
				return listener.fling(tracker.getVelocityX(), tracker.getVelocityY());
			}
		}
		return false;
	}

	/** @return whether the user touched the screen long enough to trigger a long press event. */
	public boolean isLongPressed () {
		return isLongPressed(longPressDuration);
	}

	/** @param duration
	 * @return whether the user touched the screen for as much or more than the given duration. */
	public boolean isLongPressed (float duration) {
		if (gestureStartTime == 0) return false;
		return TimeUtils.nanoTime() - gestureStartTime > (long)(duration * 1000000000l);
	}

	public boolean isPanning () {
		return panning;
	}

	public void reset () {
		gestureStartTime = 0;
		panning = false;
		inTapSquare = false;
	}
}
