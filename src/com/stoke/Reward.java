/* ************************************************************************************************
 * Copyright 2016 SUNY Binghamton
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 * ***********************************************************************************************/

package com.stoke;

import android.content.Context;
import android.os.BatteryManager;
import android.content.Intent;
import android.content.IntentFilter;

import com.google.common.base.Stopwatch;

import java.util.*;
import java.util.concurrent.*;


public class Reward {
  public static final double CAPACITY = 30000; // 30 kJ
  public static final double RATE = 32.5;
  public static final double EQUATE = 1.0;

  public Stopwatch _taskWatch  = Stopwatch.createUnstarted();
  public double _lastSec = 0;

  private IntentFilter _batFilt = null; 
  private Context _context = null;

  public Reward(Context context) {
    _context = context;
    if (_context != null) {
      _batFilt = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    }
  }

  public double getBatteryVoltage() {
    Intent batteryStatus = _context.registerReceiver(null, _batFilt);
    int mv = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
    return (mv * 0.001);
  }

  public double getBatteryAmps() {
    BatteryManager mBatteryManager = (BatteryManager)_context.getSystemService(Context.BATTERY_SERVICE);
    return (-1 * mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) * 0.000001); 
  } 

  public double getBatteryWatts() {
    return getBatteryVoltage() * getBatteryAmps();
  }

  public double batteryRate(double joules) {
    double drainrate = ((joules / CAPACITY) * 100.0) / RATE;
    return (drainrate * 60 * 60);
  }

  protected double _cachedJoules;
  public double cached() {
    return _cachedJoules;
  }

  // Programmer API
  public double valuate() { 
    return this.perInteractionEnergy();
  }

  public double SLA() { return 0; }

  public boolean equate(double r1, double r2) {
    double d = Math.abs(r1 - r2);
    return (Double.compare(d,EQUATE) <= 0);
  }

  public double perInteractionEnergy() {
    _taskWatch.stop();
    _lastSec = _taskWatch.elapsed(TimeUnit.MILLISECONDS) / 1000.0;
    _taskWatch.reset();
    _taskWatch.start();
    System.err.format("STOKE: Elapsed Seconds: %.2f\n", _lastSec);
    double joules = _lastSec * getBatteryWatts();
    _cachedJoules = joules;
    return joules;
  }

}
