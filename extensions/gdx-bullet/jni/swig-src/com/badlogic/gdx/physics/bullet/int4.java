/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.5
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.badlogic.gdx.physics.bullet;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Matrix3;

public class int4 {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected int4(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(int4 obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        gdxBulletJNI.delete_int4(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setX(int value) {
    gdxBulletJNI.int4_x_set(swigCPtr, this, value);
  }

  public int getX() {
    return gdxBulletJNI.int4_x_get(swigCPtr, this);
  }

  public void setY(int value) {
    gdxBulletJNI.int4_y_set(swigCPtr, this, value);
  }

  public int getY() {
    return gdxBulletJNI.int4_y_get(swigCPtr, this);
  }

  public void setZ(int value) {
    gdxBulletJNI.int4_z_set(swigCPtr, this, value);
  }

  public int getZ() {
    return gdxBulletJNI.int4_z_get(swigCPtr, this);
  }

  public void setW(int value) {
    gdxBulletJNI.int4_w_set(swigCPtr, this, value);
  }

  public int getW() {
    return gdxBulletJNI.int4_w_get(swigCPtr, this);
  }

  public int4() {
    this(gdxBulletJNI.new_int4__SWIG_0(), true);
  }

  public int4(int _x, int _y, int _z, int _w) {
    this(gdxBulletJNI.new_int4__SWIG_1(_x, _y, _z, _w), true);
  }

}
