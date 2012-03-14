#! /bin/sh
case $1 in
    image)
        adb shell am start -n cn.edu.shu.apps.qrreader/.ImageDecoderActivity
        ;;
    main)
        adb shell am start -n cn.edu.shu.apps.qrreader/.MainActivity
        ;;
    cleaninstall)
        rm bin/*;
        ant debug install;
        ;;
    apk)
        adb install -r -s bin/QrReader-debug.apk
        ;;
esac
