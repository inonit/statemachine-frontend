# The Rise of the State Machine

Helpful guide for world domination.

![World Domination](world-domination.jpg)

The frontend repo for a demo given for the lightning talk
The Rise of the State Machine for the Oslo Python Meetup June 2019.

## What you need

First thing you need to do is to make sure you have JDK 11 installed.

Second thing you will need is [shadow-cljs](http://shadow-cljs.org/).

## Clojure and Clojurescript

Crash course [freely available here](https://www.braveclojure.com/clojure-for-the-brave-and-true/). You also have [Clojurescript Unraveled](https://funcool.github.io/clojurescript-unraveled/) which focuses exclusively on Clojurescript.

Emacs is not recommended to learn in addition to learning Clojure, but you will want to have an editor that understands s-expressions, [so that you can do this](http://danmidwood.com/content/2014/11/21/animated-paredit.html).

Editors you can choose from:

- VS Code with [Calva](https://marketplace.visualstudio.com/items?itemName=betterthantomorrow.calva)
- Atom with [Chlorine](https://atom.io/packages/chlorine)
- IntelliJ with [Cursive](https://cursive-ide.com/index.html)
- Emacs with [CIDER](https://github.com/clojure-emacs/cider)
- Vim with [Fireplace](https://github.com/tpope/vim-fireplace)

## Stack

Clojurescript typically uses a set of libraries instead of a big framework. Each library normally has one role and one role alone to fill.

- Clojurescript
- [hiccup](https://github.com/weavejester/hiccup) (Slight lie, as it's reagent, but the HTML rendering of reagent uses the same syntax, so it's easier to read about it here)
- [reagent](https://reagent-project.github.io/) (The wrapper around react)
- [re-frame](https://github.com/Day8/re-frame) (Organizes the code and handles side effects)
- [re-com](https://github.com/Day8/re-com) (Re-usable components built on top of re-frame)


## Get it up and running

These are done in terminals.

Install all NPM deps

``` shell
npm install
```

Start the build tool (shadow-cljs). This command automatically launches a watcher, compiler, web server to browse to, live reload and an nREPL.
``` shell
shadow-cljs watch :app
```

Surf to server being run by shadow-cljs

## REPL

Connect to the started nREPL (will be printed out in the terminal where shadow-cljs is running) and in the repl type

``` clojure
(shadow/repl :app)
```

This will connect you to the REPL shadow-cljs is running for you for the browser.

## License

Copyright © 2019 inonit

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
