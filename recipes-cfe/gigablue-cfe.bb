SUMMARY = "GigaBlue CFE AddOn"
SECTION = "base"
PRIORITY = "required"
LICENSE = "proprietary"
MAINTAINER = "Gigablue"
PACKAGE_ARCH = "${MACHINE_ARCH}"

require conf/license/license-gplv2.inc
inherit deploy

PR = "r36"

S = "${WORKDIR}"

SRC_URI = "file://warning.bin file://splash.bin \
    ${@bb.utils.contains("MACHINE_FEATURES", "gigabluelcd220", "file://lcdwaitkey220.bin file://lcdwarning220.bin file://lcdsplash220.bin" , "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "gigabluelcd400", "file://lcdwaitkey400.bin file://lcdwarning400.bin file://lcdsplash400.bin" , "", d)} \
"

ALLOW_EMPTY_${PN} = "1"

do_deploy() {
    if [ -e lcdwaitkey220.bin ]; then
        install -m 0644 lcdwaitkey220.bin ${DEPLOYDIR}/lcdwaitkey.bin
        install -m 0644 lcdwarning220.bin ${DEPLOYDIR}/lcdwarning.bin
        install -m 0644 lcdsplash220.bin ${DEPLOYDIR}/lcdsplash.bin
    fi
    if [ -e lcdwaitkey400.bin ]; then
        install -m 0644 lcdwaitkey400.bin ${DEPLOYDIR}/lcdwaitkey.bin
        install -m 0644 lcdwarning400.bin ${DEPLOYDIR}/lcdwarning.bin
        install -m 0644 lcdsplash400.bin ${DEPLOYDIR}/lcdsplash.bin
    fi
    install -m 0644 warning.bin ${DEPLOYDIR}/warning.bin
    install -m 0644 splash.bin ${DEPLOYDIR}/splash.bin
}

addtask deploy before do_build after do_install
