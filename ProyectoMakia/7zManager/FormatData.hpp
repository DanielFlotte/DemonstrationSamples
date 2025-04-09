//
// Created by dan
//

#ifndef FORMATDATA_HPP
#define FORMATDATA_HPP

#include <string>
#include <vector>
#include <algorithm>

namespace m7z {
    struct FormatData;
}

struct m7z::FormatData {
    std::string name;
    bool allows_pass = false;
    bool allows_cyper_names = false;
};

namespace m7z {
    inline extern const std::vector<FormatData> known_formats = {
        FormatData{"7z", true, true},
        FormatData{"zip", true, false},
        FormatData{"xz", false, false}
    };

    inline extern const FormatData default_format_data{};

    static const FormatData& getDataFromFormat(const std::string& name) {
        auto finded = std::find_if(known_formats.begin(), known_formats.end(), [&](const FormatData& fm) {
            return fm.name == name;
        });
        if (finded != known_formats.end())
            return *finded;
        else
            return default_format_data;
    }
}


#endif //FORMATDATA_HPP
