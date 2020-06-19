package pl.poul12.matchzone.service;

import pl.poul12.matchzone.model.Appearance;

public interface AppearanceService {

    Appearance saveAppearance(Appearance appearance);

    Appearance getAppearance(Long userId);

    Appearance updateAppearance(Long userId, Appearance appearance);
}
