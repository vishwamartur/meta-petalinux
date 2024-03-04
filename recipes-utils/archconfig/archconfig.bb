DESCRIPTION = "ARCHCONFIG"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://archconfig.sh \
    file://archconfig.service \
"

inherit update-rc.d systemd

RDEPENDS:${PN} = "bash freeipmi dnf"

INITSCRIPT_NAME = "archconfig.sh"
INITSCRIPT_PARAMS = "start 99 S ."

SYSTEMD_PACKAGES="${PN}"
SYSTEMD_SERVICE:${PN}="archconfig.service"
SYSTEMD_AUTO_ENABLE:${PN}="enable"

do_install() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'true', 'false', d)}; then
        install -d ${D}${sysconfdir}/init.d/
        install -m 0755 ${WORKDIR}/archconfig.sh ${D}${sysconfdir}/init.d/
    fi

    install -d ${D}${systemd_system_unitdir}
    install -d ${D}${bindir}

    install -m 0644 ${WORKDIR}/archconfig.service ${D}${systemd_system_unitdir}
    install -m 0755 ${WORKDIR}/archconfig.sh ${D}${bindir}
}

pkg_postinst_ontarget:${PN}() {
    SOM_EEPROM="/sys/bus/i2c/devices/1-0050/eeprom"
    CARRIER_EEPROM="/sys/bus/i2c/devices/1-0051/eeprom"

    DNF_CONFIG_FILES="/etc/yum.repos.d/*.repo /etc/dnf/vars/arch /etc/rpmrc"

    SOM=$(ipmi-fru --fru-file="${SOM_EEPROM}" --interpret-oem-data | \
        awk -F': ' '/^  *FRU Board Product*/ { print tolower ($2) }' | \
        awk -F'-' '{print $2"_"$1}')

    CARRIER=$(ipmi-fru --fru-file="${CARRIER_EEPROM}" --interpret-oem-data | \
        awk -F': ' '/^  *FRU Board Product*/ { print tolower ($2) }' | \
        awk -F'-' '{print $2}')

    if [ -z "${SOM}" ] || [ -z "${CARRIER}" ]; then
        echo "ERROR: Unable to read SOM or Carrier Card version" 1>&2
        exit 1
    fi

    sed -i "s/:/:${SOM}_${CARRIER}:/" /etc/dnf/vars/arch

    sed -i "s/^arch_compat.*/& ${SOM}_${CARRIER}/" /etc/rpmrc

    for URI in ${PACKAGE_FEED_URIS}; do
        FILE_NAME=$(echo "${URI}" | awk -F"petalinux.xilinx.com/" '{print $2}' | sed 's./.-.g')
        REPO_NAME=$(echo "${URI}" | awk -F"petalinux.xilinx.com/" '{print $2}' | sed 's./. .g')
        echo -e "[oe-remote-repo-${FILE_NAME}-${SOM}_${CARRIER}]\n" \
            "name=OE Remote Repo: ${REPO_NAME} ${SOM}_${CARRIER}\n" \
            "baseurl=${URI}/${SOM}_${CARRIER}\ngpgcheck=0\n" \
                >> "/etc/yum.repos.d/oe-remote-repo-${FILE_NAME}.repo"
    done

    # K24 -> zynqmp_eg, K26 -> zynqmp_ev
    if [ "${SOM}" = "k26_smk" ] || [ "${SOM}" = "k26_sm" ]; then
        for f in ${DNF_CONFIG_FILES}; do
            sed -i s/zynqmp_eg/zynqmp_ev/g "${f}"
        done
    fi
}

FILES:${PN} += "${@bb.utils.contains('DISTRO_FEATURES','sysvinit','${sysconfdir}/init.d/archconfig.sh', '', d)} ${systemd_system_unitdir}"
