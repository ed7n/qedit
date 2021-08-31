# QEdit

[[**Homepage**](https://ed7n.github.io/qedit)]

## Building

    $ cd release && jar -x -f ../lib/edjc.jar eden && cd ..
    $ javac --class-path lib/edjc.jar -d release --release 8 --source-path src src/eden/qedit/QEdit.java && jar -c -f release/qedit.jar -e eden.qedit.QEdit -C release eden
