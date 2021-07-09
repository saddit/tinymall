import REGION_DATA from 'china-area-data'

const provinceDataList = [];
const textCodeMap = new Map();
const cityDataMap = new Map();
const countryDataMap = new Map();
const _province = REGION_DATA['86']

for (const code in _province) {
    provinceDataList.push({
        value: code,
        label: _province[code]
    })
    textCodeMap.set(_province[code], code);
}

for (let i = 0, len = provinceDataList.length; i < len; i++) {
    const provinceCode = provinceDataList[i].value
    let cityList = []
    let _cityData = REGION_DATA[provinceCode]
    for (const cityCode in _cityData) {
        cityList.push({
            value: cityCode,
            label: _cityData[cityCode]
        })
        textCodeMap.set(_cityData[cityCode], cityCode);
        //country
        let countryList = []
        let _coutryData = REGION_DATA[cityCode]
        for (const countryCode in _coutryData) {
            countryList.push({
                value: countryCode,
                label: _coutryData[countryCode]
            })
        }
        countryDataMap.set(cityCode,countryList);
    }
    cityDataMap.set(provinceCode, cityList);
}

export { provinceDataList, cityDataMap, countryDataMap, textCodeMap }