DESCRIPTION = "PetaLinux Xen supported packages"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup features_check

REQUIRED_DISTRO_FEATURES = "xen"

XEN_EXTRA_PACKAGES = ""
#XEN_EXTRA_PACKAGES = " \
#	kernel-module-xen-blkback \
#	kernel-module-xen-gntalloc \
#	kernel-module-xen-gntdev \
#	kernel-module-xen-netback \
#	kernel-module-xen-wdt \
#	xen \
#	xen-tools \
#	xen-tools-xenstat \
#	qemu-system-aarch64 \
#	${@bb.utils.contains('DISTRO_FEATURES', 'vmsep', 'qemu-keymaps', 'qemu', d)} \
#	"

RDEPENDS:${PN} = "${XEN_EXTRA_PACKAGES}"
