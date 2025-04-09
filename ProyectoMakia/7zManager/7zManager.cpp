//
// Created by dan
//
#include "7zManager.hpp"

#include <iostream>//DEBUG!
#include <ostream>
#include <filesystem>

#include "FormatData.hpp"

using namespace std;

int m7z::compress_files(const std::string& path, const CompressParameters& pass_parameters) {
    compress_files(vector<string>{path}, pass_parameters);//Write vector<string> to avoid infinite rec warning

    return 0;
}

int m7z::compress_files(const vector<string>& paths, const CompressParameters& parameters) {

    //Check if paths are all valid existing files (and if not empty?)

    string command = path_7z + " a";

    string suggestion = [&]() {
        string to_take_from = paths.front();
        if (to_take_from.back() == '/')
            to_take_from.pop_back();

        if (paths.size() > 1) {
            size_t last_slash = to_take_from.find_last_of('/');
            to_take_from = to_take_from.substr(0, last_slash);
        }

        size_t last_slash = to_take_from.find_last_of('/');
        string path = (paths.size() > 1) ? to_take_from : to_take_from.substr(0, last_slash);
        to_take_from = to_take_from.substr(last_slash + 1);

        size_t last_point = to_take_from.find_last_of('.');
        if (last_point != string::npos) //Add if is not a directory!//why?
            to_take_from = to_take_from.substr(0, last_point);

        return get_name_suggestion(path, parameters.archive_type, to_take_from);
    }();

    cout << "suggestion: " << suggestion << endl;

    command += " " + suggestion;
    for (auto& path : paths)
        command += " \"" + path + "\"";
    command += " -t" + parameters.archive_type;

    if (!parameters.pass.empty()) {
        command += " -p" + parameters.pass;
        if (getDataFromFormat(parameters.archive_type).allows_cyper_names)
            command += " -mhe="s + ((parameters.cyper_names) ? "on" : "off");
    }

    command += " -mx=" + to_string(parameters.compress_level);

    std::cout << command << std::endl;

    return system(command.c_str());
}

int m7z::decompress_file(const std::string &path, const DecompressParameters& parameters) {
    string command = path_7z + " x \"" + path + "\"";

    command += " -p" + parameters.pass;

    //check if valid in the first line?
    size_t last_slash = path.find_last_of('/');
    string out_path = path.substr(0, last_slash);
    command += " -o\"" + out_path + "\"";

    command += " -aou";

    std::cout << command << std::endl;

    return system(command.c_str());
}

string m7z::get_name_suggestion(const string& path, const string& archive_type, const string& suggestion) {

    filesystem::path next = path + "/" + suggestion + "." + archive_type;
    for (size_t i = 1;; ++i) { //Problems with C++17?
        if (!filesystem::exists(next))
            break;
        next = path + "/" + suggestion + "_" + to_string(i) + "." + archive_type;
    }

    return "\"" + next.string() + "\"";
}
