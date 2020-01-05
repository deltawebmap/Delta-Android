package com.romanport.deltawebmap.Framework.Session.Actions;

import com.romanport.deltawebmap.Framework.API.Echo.CreateSession.CreateSessionResponse;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Icons.IconResponseData;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Structures.StructuresResponse;
import com.romanport.deltawebmap.Framework.Session.DeltaServerCallback;
import com.romanport.deltawebmap.Framework.Session.DeltaServerSession;
import com.romanport.deltawebmap.MainActivity;

public class ConfigureMapAction extends DeltaServerCallback<MainActivity> {

    IconResponseData icons;
    CreateSessionResponse session;
    StructuresResponse structures;

    @Override
    public void Run(DeltaServerSession session) throws Exception {
        //Here is where we obtain map data and icon data
        this.icons = session.GetIcons();
        this.session = session.GetSession();
        this.structures = session.GetStructures();
    }

    @Override
    public void RunMainThread(MainActivity activity) {
        //Configure map
        activity.SetMap(icons, session.mapData, structures);
    }
}
