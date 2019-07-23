# tic-tac-toe

The project uses [Midje](https://github.com/marick/Midje/).

## How to run the tests

`lein midje` will run all tests.

`lein midje namespace.*` will run only tests beginning with "namespace.".

`lein midje :autotest` will run all the tests indefinitely. It sets up a
watcher on the code files. If they change, only the relevant tests will be
run again.

---

Stalemate game:

```
X O X
O X X
O X O
:top-left
:top-middle
:top-right
:centre-left
:centre-middle
:bottom-left
:centre-right
:bottom-right
:bottom-middle
```

Game won on 9th play:

```
X O X
O X X
O O X
:top-left
:top-middle
:top-right
:centre-left
:centre-middle
:bottom-left
:centre-right
:bottom-middle
:bottom-right
```