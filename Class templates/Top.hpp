//
// Created by dan
//

#ifndef DEMOSTRACION_TOP_HPP
#define DEMOSTRACION_TOP_HPP
#include <list>
#include <set>
#include <initializer_list>

template<typename T> class Top {
    template<typename T_> friend void swap(Top<T_>&, Top<T_>&);
    template<typename T_> friend bool operator==(const Top<T_>&, const Top<T_>&);
public:
    using const_iterator = typename std::multiset<T>::const_iterator;

    explicit Top(size_t);
    Top(const Top&) = default;
    Top(Top&&) noexcept = default;
    Top& operator=(const Top&) = default;
    Top& operator=(Top&&) noexcept = default;

    const_iterator begin() const;
    const_iterator end() const;
    const_iterator cbegin() const;
    const_iterator cend() const;

    void add(const T&);
    void add(T&&);
    void add(std::initializer_list<T>);
    template<typename ...Args> void emplace(Args&&...);
    template<typename It> void add(It, It);
    void clear();

    void setLimit(size_t);
    bool empty() const;
    size_t size() const;
    size_t size_limit() const;
private:
    std::multiset<T> data;
    size_t data_size_limit;
};

template<typename T>
void swap(Top<T>& top1, Top<T>& top2) {
    using std::swap;
    swap(top1.data, top2.data);
    swap(top1.data_size_limit, top2.data_size_limit);
}

template<typename T>
bool operator==(const Top<T>& top1, const Top<T>& top2) {
    return top1.data == top2.data && top1.data_size_limit == top2.data_size_limit;
}

template<typename T>
Top<T>::Top(size_t data_size_limit_): data_size_limit(data_size_limit_), data()
{}

template<typename T>
typename Top<T>::const_iterator Top<T>::begin() const {
    return this->cbegin();
}

template<typename T>
typename Top<T>::const_iterator Top<T>::end() const {
    return this->cend();
}

template<typename T>
typename Top<T>::const_iterator Top<T>::cbegin() const {
    return data.cbegin();
}

template<typename T>
typename Top<T>::const_iterator Top<T>::cend() const {
    return data.cend();
}

template<typename T>
void Top<T>::add(const T& element) {
    this->add(T(element));
}

template<typename T>
void Top<T>::add(T&& element) {
    if (data.size() < data_size_limit) {
        data.emplace(std::forward<T>(element));
        return;
    }

    if (*data.begin() < element) {
        data.erase(data.begin());
        data.emplace(std::forward<T>(element));
    }
}

template<typename T>
void Top<T>::add(std::initializer_list<T> il) {
    for (auto& i : il)
        this->add(T(i));
}

template<typename T>
template<typename... Args>
void Top<T>::emplace(Args&&... args) {
    this->add(T(args...));
}

template<typename T>
template<typename It>
void Top<T>::add(It beg, It end) {
    for (; beg != end; ++beg)
        this->add(*beg);
}

template<typename T>
void Top<T>::clear() {
    data.clear();
}

template<typename T>
void Top<T>::setLimit(size_t new_limit) {
    data_size_limit = new_limit;
    if (data.size() > data_size_limit)
        data.erase(data.begin(), data.begin() + (data.size() + data_size_limit));
}

template<typename T>
bool Top<T>::empty() const {
    return data.empty();
}

template<typename T>
size_t Top<T>::size() const {
    return data.size();
}

template<typename T>
size_t Top<T>::size_limit() const {
    return this->data_size_limit;
}
#endif //DEMOSTRACION_TOP_HPP
