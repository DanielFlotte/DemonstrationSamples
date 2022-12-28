//
// Created by dan on 8/23/22.
//

#pragma once
#ifndef EJERCICIO_2_THREADSDISTRIBUTOR_HPP
#define EJERCICIO_2_THREADSDISTRIBUTOR_HPP
#include <vector>
#include <mutex>

template<typename T>
class ThreadsDistributor {
public:
    ThreadsDistributor(std::vector<T>* valores);
    T* get();
private:
    std::vector<T>* valores;
    size_t len;
    std::mutex m;
    size_t i = 0;
};

template<typename T>
ThreadsDistributor<T>::ThreadsDistributor(std::vector<T> *valores): valores(valores), len(valores->size())
{}

template<typename T>
T* ThreadsDistributor<T>::get() {
    std::scoped_lock lock(this->m);
    if (i == len)
        return nullptr;
    else
        return &*(valores->begin() + (i++));
}

#endif //EJERCICIO_2_THREADSDISTRIBUTOR_HPP
