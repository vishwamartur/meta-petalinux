#!/bin/sh

# Package manager configuration files should have been updated to contain the
# correct carrier card and SOC variant values on first boot - if they are not
# present this configuration may not have worked

error() { echo "ERROR: ${1}" 1>&2; exit 1; }

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
    error "Unable to read SOM or Carrier Card version"
fi

if ! grep "${SOM}_${CARRIER}" /etc/dnf/vars/arch > /dev/null; then
    error "Carrier card configuration missing from DNF config"
fi

if ! grep "${SOM}_${CARRIER}" /etc/rpmrc > /dev/null; then
    error "Carrier card configuration missing from RPM config"
fi

if [ "${SOM}" = "smk_k26" ]; then
    if grep "zynqmp_eg" "${DNF_CONFIG_FILES}" > /dev/null; then
        error "System configured for zynqmp_eg but running on zynqmp_ev"
    fi
elif [ "${SOM}" = "smk_k24" ]; then
    if grep "zynqmp_ev" "${DNF_CONFIG_FILES}" > /dev/null; then
        error "System configured for zynqmp_ev but running on zynqmp_eg"
    fi
fi
