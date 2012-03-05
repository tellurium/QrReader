#! /bin/sh
case $1 in
    main)
        adb shell am start -n cn.edu.shu.apps.qrreader/.MainActivity
        ;;
    cleaninstall)
        rm bin/*;
        ant debug install;
        ;;
esac
