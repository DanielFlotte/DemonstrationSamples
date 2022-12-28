#include <iostream>
#include <string>
#include <vector>
#include <array>
#include <thread>
#include <mutex>
#include <algorithm>
#include <chrono>
#include <cmath>
#include "ThreadsDistributor.hpp"

using namespace std;
using Distributor = ThreadsDistributor<ulong>;

void thread_action(Distributor&);
void sequence(ulong);
ulong combine(ulong, ulong, ulong);
bool is_pandigital(ulong);

mutex cout_mut;

int main(int argc, char** argv) {
    if (argc == 1) {
        cerr << "Number of threads?" << endl;
        return -1;
    }

    const ulong threads_required = stoull(argv[1]);
    const ulong hardware_threads = std::thread::hardware_concurrency();
    const ulong threads_available = (hardware_threads == 0) ? 2 : hardware_threads;
    const ulong threads_to_use = min(threads_available, threads_required);

    vector<ulong> numbers; numbers.reserve(10000 - 1 + 1);
    ulong count = 1;
    for (; count < 10000; ++count)
        numbers.push_back(count);
    Distributor distributor(&numbers);

    vector<thread> threads(threads_to_use - 1);

    for (auto& t : threads)
        t = thread(thread_action, std::ref(distributor));
    thread_action(distributor);

    for (auto& t : threads)
        t.join();

    cout << "END" << endl;
    return 0;
}

void thread_action(Distributor& distributor) {
    while (true) {
        ulong* getted = distributor.get();
        if (getted == nullptr)
            break;
        sequence(*getted);
    }
}

void sequence(ulong i) {
    for (ulong j = i; j < 10000; ++j) {
        if (is_pandigital(combine(i, j, i * j))) {
            scoped_lock lock(cout_mut);
            cout << i << " * " << j << " = " << i * j << endl;
        }
    }
}

ulong combine(ulong x, ulong y, ulong z) {
    return stoull(to_string(x) + to_string(y) + to_string(z));
}

bool is_pandigital(ulong n) {
    const ulong digits = log10(n) + 1;
    vector<bool> digits_existance(digits, false);
    while (n != 0) {
        ulong div = n / 10;
        ulong digito = n - div * 10;
        n = div;

        if (digito - 1 >= digits)
            return false;
        digits_existance.at(digito - 1) = true;
    }

    return all_of(digits_existance.begin(), digits_existance.end(), [](bool b){return b;});
}

