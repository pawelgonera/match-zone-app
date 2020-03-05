package pl.poul12.matchzone.service;

import pl.poul12.matchzone.model.Appearance;

public interface AppearanceService {

    Appearance getAppearance(String username);

    Appearance updateAppearance(String username, Appearance appearance);
}
