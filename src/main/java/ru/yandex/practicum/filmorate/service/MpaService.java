package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;
import java.util.Optional;

@Service
public class MpaService {
    MpaDbStorage storage;

    public MpaService(MpaDbStorage storage) {
        this.storage = storage;
    }

    public List<Mpa> getAllMpa() {
        return storage.getAllMpa();
    }

    public Optional<Mpa> getMpaById(Long mpaId) {
        return storage.getMpaById(mpaId);
    }
}
