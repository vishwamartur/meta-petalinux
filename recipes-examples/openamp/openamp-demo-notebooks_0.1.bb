DESCRIPTION = "Jupyter notebooks for openAMP"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=268f2517fdae6d70f4ea4c55c4090aa8"

inherit jupyter-examples

REPO ?= "git://github.com/Xilinx/OpenAMP-notebooks.git;protocol=https"
SRCREV ?= "75ca92a939e72a03194d66a67708a31e1dcf958b"
BRANCH ?= "main"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
SRC_URI = "${REPO};${BRANCHARG}"
PV .= "+git${SRCPV}"
S = "${WORKDIR}/git/openamp"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
COMPATIBLE_MACHINE:versal = "versal"

RDEPENDS:${PN} = " packagegroup-petalinux-jupyter \
                   packagegroup-petalinux-openamp"

do_install() {
    install -d ${D}/${JUPYTER_DIR}/openamp-notebooks
    install -d ${D}/${JUPYTER_DIR}/openamp-notebooks/pics

    install -m 0755 ${S}/*.ipynb    ${D}/${JUPYTER_DIR}/openamp-notebooks
    install -m 0755 ${S}/pics/*.png ${D}/${JUPYTER_DIR}/openamp-notebooks/pics
}
