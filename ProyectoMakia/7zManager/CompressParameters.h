//
// Created by dan
//

#ifndef PASSPARAMETERS_H
#define PASSPARAMETERS_H
#include <string>

namespace m7z {
    struct CompressParameters {
        std::string archive_type = "7z";
        std::string pass = "";
        bool cyper_names = false;
        uint compress_level = 5;
    };
}

#endif //PASSPARAMETERS_H
