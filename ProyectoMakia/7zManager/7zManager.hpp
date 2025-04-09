//
// Created by dan
//

#ifndef INC_7ZMANAGER_HPP
#define INC_7ZMANAGER_HPP
#include <vector>
#include <string>
#include "CompressParameters.h"
#include "DecompressParameters.h"

inline extern std::string path_7z = "/bin/7zz";

namespace m7z {
    int compress_files(const std::string& path, const CompressParameters&);
    int compress_files(const std::vector<std::string>& paths, const CompressParameters&);

    int decompress_file(const std::string& path, const DecompressParameters&);

    std::string get_name_suggestion(const std::string&, const std::string&, const std::string& suggestion = "archive");
}

#endif //INC_7ZMANAGER_HPP
