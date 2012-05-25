#! /bin/sh
case $1 in
    main)
        adb shell am start -n cn.edu.shu.apps.qrreader/.MainActivity
        ;;
    operate)
        adb shell am start -n cn.edu.shu.apps.qrreader/.OperatingActivity
        ;;
    cleaninstall)
        rm bin/*;
        ant debug install;
        ;;
    apk)
        adb install -r -s bin/QrReader-debug.apk
        ;;
esac
