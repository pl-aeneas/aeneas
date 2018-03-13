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

[We provide a technical document, detailing raw data and results for our experiments](https://github.com/pl-aeneas/aeneas/blob/master/supp.pdf). For further details, please consult the technical document along with the [MapsWithMe application](https://github.com/pl-aeneas/aeneas-mapsme) repository.
