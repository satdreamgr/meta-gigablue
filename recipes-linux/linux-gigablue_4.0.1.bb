SUMMARY = "Linux kernel for ${MACHINE}"
SECTION = "kernel"
LICENSE = "GPLv2"

COMPATIBLE_MACHINE = "gbquadplus"

SRC_URI[md5sum] = "b78daefd10700006ef8b48d01c5e17b7"
SRC_URI[sha256sum] = "38aafba52d6492c35bc3e7d1ea782ea21c84c69931d963de8ce80297719c7634"

LIC_FILES_CHKSUM = "file://${WORKDIR}/linux-${PV}/COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

MACHINE_KERNEL_PR = "r0"

# By default, kernel.bbclass modifies package names to allow multiple kernels
# to be installed in parallel. We revert this change and rprovide the versioned
# package names instead, to allow only one kernel to be installed.
PKG_${KERNEL_PACKAGE_NAME}-base = "kernel-base"
PKG_${KERNEL_PACKAGE_NAME}-image = "kernel-image"
RPROVIDES_${KERNEL_PACKAGE_NAME}-base = "kernel-${KERNEL_VERSION}"
RPROVIDES_${KERNEL_PACKAGE_NAME}-image = "kernel-image-${KERNEL_VERSION}"

SRC_URI += "http://archiv.openmips.com/gigablue-linux-${PV}-20150828.tgz \
    file://kernel-gcc6.patch \
    file://add-dmx-source-timecode.patch \
    file://iosched-slice_idle-1.patch \
    file://bcmgenet.patch \
    file://0002-add-brcm-chips.patch \
    file://0001-Support-TBS-USB-drivers-for-4.0.1-kernel.patch \
    file://0001-TBS-fixes-for-4.0.1-kernel.patch \
    file://0001-STV-Add-PLS-support.patch \
    file://0001-STV-Add-SNR-Signal-report-parameters.patch \
    file://blindscan2.patch \
    file://0001-stv090x-optimized-TS-sync-control.patch \
    file://defconfig \
    "

inherit kernel machine_kernel_pr

S = "${WORKDIR}/linux-${PV}"
B = "${WORKDIR}/build"

export OS = "Linux"
KERNEL_OBJECT_SUFFIX = "ko"
KERNEL_OUTPUT = "vmlinux"
KERNEL_OUTPUT_DIR = "."
KERNEL_IMAGETYPE = "vmlinux"
KERNEL_IMAGEDEST = "/tmp"

FILES_${KERNEL_PACKAGE_NAME}-image = "${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}.gz"


kernel_do_install_append() {
    ${STRIP} ${D}${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
    gzip -9c ${D}${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION} > ${D}${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}.gz
    rm ${D}${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
}

pkg_postinst_kernel-image () {
    if [ "x$D" == "x" ]; then
        if [ -f /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}.gz ] ; then
            flash_erase /dev/${MTD_KERNEL} 0 0
            nandwrite -p /dev/${MTD_KERNEL} /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}.gz
            rm -f /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}.gz
        fi
    fi
    true
}

do_rm_work() {
}
