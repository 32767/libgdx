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

package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveBy;
import com.badlogic.gdx.scenes.scene2d.actions.Parallel;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.tests.utils.GdxTest;

public class ActionSequenceTest extends GdxTest implements OnActionCompleted {

	@Override
	public boolean needsGL20 () {
		return false;
	}

	Image img;
	Image img2;
	Image img3;
	Stage stage;
	Texture texture;

	@Override
	public void create () {
		stage = new Stage(480, 320, true);
		texture = new Texture(Gdx.files.internal("data/badlogic.jpg"), false);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		img = new Image(new TextureRegion(texture));
		img.width = img.height = 100;
		img.originX = 50;
		img.originY = 50;
		img.x = img.y = 100;

		img2 = new Image(new TextureRegion(texture));
		img2.width = img2.height = 100;
		img2.originX = 50;
		img2.originY = 50;
		img2.x = img2.y = 100;

		img3 = new Image(new TextureRegion(texture));
		img3.width = img3.height = 100;
		img3.originX = 50;
		img3.originY = 50;
		img3.x = img3.y = 100;

		stage.addActor(img);
		stage.addActor(img2);
		stage.addActor(img3);
		
		img.action(Sequence.$());
		img2.action(Parallel.$(Sequence.$(), MoveBy.$(100, 0, 1)));
		img3.action(Sequence.$(Parallel.$(MoveBy.$(100, 200, 2))));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void completed (Action action) {
		System.out.println("completed");
	}

	@Override
	public void dispose () {
		stage.dispose();
		texture.dispose();
	}
}
