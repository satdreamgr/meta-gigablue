# We know about rtc
include conf/machine/include/build-extras.inc

MTD_KERNEL = "mtd2"
MTD_ROOTFS = "mtd0"
KERNEL_FILE = "kernel.bin"
ROOTFS_FILE = "rootfs.bin"

MACHINE_ESSENTIAL_EXTRA_RDEPENDS = " \
    gigablue-dvb-modules-${MACHINE} \
    gigablue-ddbootup \
    "

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS = " \
    kernel-module-cdfs \
    kernel-module-zram \
    kernel-module-ext2 \
    kernel-module-ext3 \
    kernel-module-ext4 \
    kernel-module-cifs \
    kernel-module-autofs4 \
    "

MACHINE_EXTRA_RRECOMMENDS = " \
    gstreamer1.0-plugin-dvbmediasink \
    gigablue-cfe \
    ${@bb.utils.contains('DISTRO_FEATURES', 'audioeffect', 'enigma2-plugin-systemplugins-audioeffect' , '', d)} \
    "

MACHINE_EXTRA_RDEPENDS = "\
    enigma2-plugin-systemplugins-vfdcontrol \
"

include conf/machine/include/autoload-filesystems.inc

PREFERRED_PROVIDER_virtual/blindscan-dvbc = "gigablue-blindscan-utils"
PREFERRED_PROVIDER_virtual/blindscan-dvbs = "gigablue-blindscan-utils"

DVBMEDIASINK_CONFIG = "--with-wmv"

TARGET_ARCH = "mipsel"
DEFAULTTUNE = "mips32el"
require conf/machine/include/tune-mips32.inc

PREFERRED_PROVIDER_virtual/kernel = "linux-gigablue"

KERNEL_IMAGETYPE = "vmlinux"
KERNEL_OUTPUT = "${KERNEL_IMAGETYPE}"
KERNEL_CONSOLE = "ttyS0,115200n8"

MACHINE_FEATURES += "3dtv alsa hdmicec switchoff bootsplash kernelwifi"

BOOTLOGO_FILENAME = "splash.bin"

IMGDEPLOYDIR ?= "${DEPLOY_DIR_IMAGE}"
IMAGEVERSION := "${DISTRO_NAME}-${DISTRO_VERSION}-${DATE}"
IMAGEVERSION[vardepsexclude] = "DATE"

IMAGE_CMD_ubi_prepend = " \
    rm -rf ${IMAGE_ROOTFS}/boot/*; \
    rm -rf ${IMAGE_ROOTFS}/tmp/*; \
    "

IMAGE_CMD_ubi_append = " \
    mkdir -p ${IMGDEPLOYDIR}/${IMAGEDIR}; \
    cp ${DEPLOY_DIR_IMAGE}/lcd*.bin ${IMGDEPLOYDIR}/${IMAGEDIR}; \
    cp ${DEPLOY_DIR_IMAGE}/splash.bin ${IMGDEPLOYDIR}/${IMAGEDIR}; \
    cp ${DEPLOY_DIR_IMAGE}/warning.bin ${IMGDEPLOYDIR}/${IMAGEDIR}; \
    mv ${IMGDEPLOYDIR}/${IMAGE_NAME}.rootfs.ubi ${IMGDEPLOYDIR}/${IMAGEDIR}/${ROOTFS_FILE}; \
    gzip -f -c ${DEPLOY_DIR_IMAGE}/vmlinux-${MACHINE}.bin > ${IMGDEPLOYDIR}/${IMAGEDIR}/${KERNEL_FILE}; \
    echo ${IMAGEVERSION} > ${IMGDEPLOYDIR}/${IMAGEDIR}/imageversion; \
    echo "rename this file to 'force' to force an update without confirmation" > ${IMGDEPLOYDIR}/${IMAGEDIR}/noforce; \
    cd ${IMGDEPLOYDIR}; \
    zip -r ${DISTRO_NAME}-${DISTRO_VERSION}-${MACHINE}_usb.zip ${IMAGEDIR}/*; \
    rm -rf ${IMAGEDIR}; \
    "
