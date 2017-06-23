# simple-cqrs
Clojure version of https://github.com/gregoryyoung/m-r

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a backend server for the application, navigate to `simple-cqrs` subdirectory and run:

    lein ring server-headless

To launch a GUI, jump to `cqrs-gui` subfolder and type:

    lein ring server

## License

Coded in 2017 by Paweł Stroiński and released as a public domain.

The HTML/CSS templates which can be found in the `cqrs-gui/resources/public` subfolder were copied from the original project.
