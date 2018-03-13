# Aeneas

Prerequisites
------------

An android SDK is required to build the Aeneas library. Please follow the [android developer instructions](https://developer.android.com/studio/index.html) for installing an android SDK.

Once installed, please set the ANDROID_HOME environment variable to point to the location of your android SDK.


Installation
------------

Simply run ```ant release``` to build the Aeneas library. Once built, $AENEAS_HOME/bin will contain a jar named classes.jar that can be used to link into android projects.


Usage
------------

For an example of how to use Aeneas, we refer interested programmer to the [Aeneas Model](https://github.com/pl-aeneas/aeneas-model), in particular, [Simulator.java](https://github.com/pl-aeneas/aeneas-model/blob/master/src/model/Simulator.java) shows a complete usage of Aeneas, including all initialization, with requiring an Android application.

For a real-world use case example of Aeneas, we refer interested developers to our modified [MapsWithMe application](https://github.com/pl-aeneas/aeneas-mapsme).

Technical
------------

[We provide a technical document, detailing experiments and results](https://github.com/pl-aeneas/aeneas/blob/master/supp.pdf). 

# Specification

Knobs
------------

Aeneas has main programming abstractions: ```Knob``` and ```Reward```. 

Knobs are either ```Discrete``` or ```Inferred``` and represent program alternative behaviors that Aeneas can select.

```
// Create a Discrete Knob with 3 settings, 10000, 5000, and 1000
Knob k1 = new DiscreteKnob("gps-interval", KnobValT.haveIntegers(10000,5000,1000);

// Create an Inferred Knob with a range between 10000 - 500, including a seed value 1000
Knob k2 = new InferredKnob("gps-interval", 10000, 500, new Integer[]{1000});

```

Rewards
------------

Rewards are programmer defined Quality-of-Service (QoS) specifications that the Aeneas machice will optimizer for. We provide a default ```Reward``` class that uses energy as the reward. For Android systems, we require an android ```Context``` be supplied in order to read the ```BatteryManager``` API. This default implementation will cause Aeneas to simply optimize for the minimim amount of energy consumption.

```
Reward reward = new Reward(getApplicationContext());
```

Programmers may specific a specific service level agreement for Aeneas to meet, i.e., instead of minimizing energy consumption, Aeneas can find application configurations that attempt to meet some supplied SLA. One example of this is targeting a battery drain rate. This is done by overriding the ```valueate``` and ```SLA``` methods of the ```Reward``` class. For example, the following will create a reward for Aeneas to target 30% battery drain rate per hour.

```
Reward reward = new Reward(getApplicationContext()){
  @Override 
  public double valuate() {
    double e = this.perInteractionEnergy();
    return this.batteryRate(e);
  }
  @Override
  public double SLA() {
    return 0.20;
  }
};

```

Methods ```perInteractionEnergy``` and ```batteryRate``` are provided by Aeneas. The former tracks the energy between subsequent interactions of the Aeneas machine, the latter converts joules to drain rate per hour.

Machine
------------

The Aeneas machine is created with a optimization policy, a set of Knobs, and Reward.

```
AeneasMachine machine = new AeneasMachine(new StochasticPolicyType.VDBE, new Knob[]{k1, k2}, reward);
```

Once created, the machine may be started and stopped with ```machine.start()``` and ```machine.stop()``` respectively. ```start``` will start the Aeneas thread, at which point the Aeneas optimizer will search for ideal settings.

One may read the value of a knob using the ```read``` method.

```
int setting = machine.read(update);
```


